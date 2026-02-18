package org.zimid.bookingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zimid.bookingservice.dto.BookingRequest;
import org.zimid.bookingservice.dto.BookingResponse;
import org.zimid.bookingservice.exception.BookingNotFoundException;
import org.zimid.bookingservice.exception.InvalidBookingException;
import org.zimid.bookingservice.exception.SlotNotAvailableException;
import org.zimid.bookingservice.messaging.BookingEvent;
import org.zimid.bookingservice.model.Booking;
import org.zimid.bookingservice.model.Province;
import org.zimid.bookingservice.model.ServiceType;
import org.zimid.bookingservice.repository.BookingRepository;
import org.zimid.bookingservice.repository.ProvinceRepository;
import org.zimid.bookingservice.repository.ServiceTypeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ProvinceRepository provinceRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final RabbitTemplate rabbitTemplate;

    private static final int MAX_BOOKINGS_PER_SLOT = 3;
    private static final String BOOKING_EXCHANGE = "booking.exchange";
    private static final String BOOKING_CREATED_ROUTING_KEY = "booking.created";
    private static final String BOOKING_UPDATED_ROUTING_KEY = "booking.updated";
    private static final String BOOKING_CANCELLED_ROUTING_KEY = "booking.cancelled";


    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        log.info("Creating Booking for {}", request.getPhoneNumber());

        //Validate province exists
        Province province = provinceRepository.findById(request.getProvinceId())
                .orElseThrow(() -> new InvalidBookingException("Province not found"));

        if (!province.getActive()){
            throw new InvalidBookingException("Selected province office is not currently accepting bookings");
        }

        //Validate service exists
        ServiceType service = serviceTypeRepository.findById(request.getServiceId())
                .orElseThrow(() -> new InvalidBookingException("Service not found"));

        if (!service.getActive()){
            throw new InvalidBookingException("Selected service is not currently available");
        }

        //Validate appointment date is not in the past
        if(request.getAppointmentDate().isBefore(LocalDate.now())){
            throw new InvalidBookingException("Appointment date cannot be in the past");
        }

        // Validate appointment date is not too far in the future (e.g., max 90 days)
        if (request.getAppointmentDate().isAfter(LocalDate.now().plusDays(90))) {
            throw new InvalidBookingException("Appointments can only be booked up to 90 days in advance");
        }

        // Check slot availability
        if (!isSlotAvailable(province.getId(), request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new SlotNotAvailableException("Selected time slot is not available");
        }

        // Check daily capacity
        Long bookingsOnDate = bookingRepository.countBookingsForProvinceOnDate(
                province.getId(),
                request.getAppointmentDate()
        );

        if (bookingsOnDate >= province.getDailyCapacity()) {
            throw new SlotNotAvailableException("Office has reached capacity for selected date");
        }

        //Create Booking
        Booking booking = Booking.builder()
                .bookingReference(generateBookingReference())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .province(province)
                .service(service)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .status(Booking.BookingStatus.CONFIRMED)
                .channel(request.getChannel())
                .notes(request.getNotes())
                .confirmedAt(LocalDateTime.now())
                .build();

        booking = bookingRepository.save(booking);
        log.info("Booking created successfully: {}", booking.getBookingReference());

        //Publish event to RabbitMQ for notifications
        publishBookingEvent(booking, "CREATED");

        return mapToResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingByReference(String reference) {
        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: " + reference));
        return mapToResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> searchBookings(String searchTerm) {
        List<Booking> bookings = bookingRepository.searchBookings(searchTerm);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByPhoneNumber(String phoneNumber) {
        List<Booking> bookings = bookingRepository.findByPhoneNumber(phoneNumber);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getBookingsByEmail(String email) {
        List<Booking> bookings = bookingRepository.findByEmail(email);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse updateBookingStatus(String reference, Booking.BookingStatus newStatus) {
        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: " + reference));

        Booking.BookingStatus oldStatus = booking.getStatus();
        booking.setStatus(newStatus);

        //Update timestamps based on status
        switch (newStatus) {
            case CONFIRMED:
                booking.setConfirmedAt(LocalDateTime.now());
                break;
            case COMPLETED:
                booking.setCompletedAt(LocalDateTime.now());
                break;
            case CANCELLED:
                booking.setCancelledAt(LocalDateTime.now());
                break;
        }

        booking = bookingRepository.save(booking);
        log.info("Booking {} status updated from {} to {}", reference, oldStatus, newStatus);

        //Publish event
        publishBookingEvent(booking, "UPDATED");

        return mapToResponse(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(String reference, String reason) {
        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: " + reference));

        if (booking.getStatus().equals(Booking.BookingStatus.COMPLETED)) {
            throw new InvalidBookingException("Cannot cancel a completed booking");
        }

        if (booking.getStatus().equals(Booking.BookingStatus.CANCELLED)) {
            throw  new InvalidBookingException("Booking is already cancelled");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking.setCancelledAt(LocalDateTime.now());

        bookingRepository.save(booking);
        log.info("Booking {} cancelled. Reason: {}", reference, reason);

        //Publish Event
        publishBookingEvent(booking, "CANCELLED");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSlotAvailable(Long provinceId, LocalDate date, LocalTime time) {
        Long bookingsInSlot = bookingRepository.countBookingsForTimeSlot(provinceId, date, time);
        return bookingsInSlot < MAX_BOOKINGS_PER_SLOT;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalTime> getAvailableTimeSlots(Long provinceId, LocalDate date) {
        // Standard working hours time slots
        List<LocalTime> allSlots = List.of(
                LocalTime.of(8, 0), LocalTime.of(8, 30), LocalTime.of(9, 0),
                LocalTime.of(9, 30), LocalTime.of(10, 0), LocalTime.of(10, 30),
                LocalTime.of(11, 0), LocalTime.of(11, 30), LocalTime.of(12, 0),
                LocalTime.of(12, 30), LocalTime.of(14, 0), LocalTime.of(14, 30),
                LocalTime.of(15, 0), LocalTime.of(15, 30), LocalTime.of(16, 0)
        );

        return allSlots.stream()
                .filter(slot -> isSlotAvailable(provinceId, date, slot))
                .collect(Collectors.toList());
    }

    private String generateBookingReference() {
        Random random = new Random();
        int year = LocalDate.now().getYear();
        int randomNum = 1000 + random.nextInt(9000);
        return String.format("ZW-%d-%04d", year, randomNum);
    }

    private void publishBookingEvent(Booking booking, String eventType) {
        try {
            BookingEvent event = BookingEvent.builder()
                    .bookingReference(booking.getBookingReference())
                    .fullName(booking.getFullName())
                    .phoneNumber(booking.getPhoneNumber())
                    .email(booking.getEmail())
                    .provinceName(booking.getProvince().getName())
                    .serviceName(booking.getService().getName())
                    .appointmentDate(booking.getAppointmentDate())
                    .appointmentTime(booking.getAppointmentTime())
                    .status(booking.getStatus().name())
                    .eventType(eventType)
                    .timestamp(LocalDateTime.now())
                    .build();

            String routingKey = switch (eventType) {
                case "CREATED" -> BOOKING_CREATED_ROUTING_KEY;
                case "CANCELLED" -> BOOKING_CANCELLED_ROUTING_KEY;
                default -> BOOKING_UPDATED_ROUTING_KEY;
            };

            rabbitTemplate.convertAndSend(BOOKING_EXCHANGE, routingKey, event);
            log.info("Published booking event: {} for {}", eventType, booking.getBookingReference());
        } catch (Exception e) {
            log.error("Failed to publish booking event", e);
            // Don't fail the booking operation if event publishing fails
        }
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .fullName(booking.getFullName())
                .dateOfBirth(booking.getDateOfBirth())
                .phoneNumber(booking.getPhoneNumber())
                .email(booking.getEmail())
                .province(BookingResponse.ProvinceDTO.builder()
                        .id(booking.getProvince().getId())
                        .code(booking.getProvince().getCode())
                        .name(booking.getProvince().getName())
                        .officeName(booking.getProvince().getOfficeName())
                        .address(booking.getProvince().getAddress())
                        .phone(booking.getProvince().getPhone())
                        .build())
                .service(BookingResponse.ServiceTypeDTO.builder()
                        .id(booking.getService().getId())
                        .code(booking.getService().getCode())
                        .name(booking.getService().getName())
                        .description(booking.getService().getDescription())
                        .durationMinutes(booking.getService().getDurationMinutes())
                        .fee(booking.getService().getFee().toString())
                        .currency(booking.getService().getCurrency())
                        .build())
                .appointmentDate(booking.getAppointmentDate())
                .appointmentTime(booking.getAppointmentTime())
                .status(booking.getStatus())
                .channel(booking.getChannel())
                .notes(booking.getNotes())
                .cancellationReason(booking.getCancellationReason())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .confirmedAt(booking.getConfirmedAt())
                .completedAt(booking.getCompletedAt())
                .cancelledAt(booking.getCancelledAt())
                .build();
    }
}
