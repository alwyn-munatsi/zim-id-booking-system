package org.zimid.bookingservice.service;

import org.zimid.bookingservice.dto.BookingRequest;
import org.zimid.bookingservice.dto.BookingResponse;
import org.zimid.bookingservice.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);

    BookingResponse getBookingByReference(String reference);

    List<BookingResponse> searchBookings(String searchTerm);

    List<BookingResponse> getBookingsByPhoneNumber(String phoneNumber);

    List<BookingResponse> getBookingsByEmail(String email);

    BookingResponse updateBookingStatus(String reference, Booking.BookingStatus newStatus);

    void cancelBooking(String reference, String reason);

    boolean isSlotAvailable(Long provinceId, LocalDate date, LocalTime time);

    List<LocalTime> getAvailableTimeSlots(Long provinceId, LocalDate date);
}
