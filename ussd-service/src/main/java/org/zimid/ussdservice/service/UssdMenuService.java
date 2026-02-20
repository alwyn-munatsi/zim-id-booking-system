package org.zimid.ussdservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zimid.ussdservice.client.BookingServiceClient;
import org.zimid.ussdservice.dto.BookingRequest;
import org.zimid.ussdservice.dto.BookingResponse;
import org.zimid.ussdservice.dto.ProvinceResponse;
import org.zimid.ussdservice.dto.ServiceTypeResponse;
import org.zimid.ussdservice.model.UssdSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UssdMenuService {

    private final SessionService sessionService;
    private final BookingServiceClient bookingServiceClient;

    public String processUssdRequest(String sessionId, String phoneNumber, String text) {
        log.info("Processing USSD request - Session: {}, Phone: {}, Text: {}", sessionId, phoneNumber, text);

        // Get or create session
        UssdSession session = sessionService.getSession(sessionId);
        if (session == null) {
            session = UssdSession.builder()
                    .sessionId(sessionId)
                    .phoneNumber(phoneNumber)
                    .currentMenu("MAIN_MENU")
                    .menuLevel(0)
                    .build();
        }

        // Process based on current menu and user input
        String response;
        if (text.isEmpty()) {
            // First request - show main menu
            response = showMainMenu();
            session.setCurrentMenu("MAIN_MENU");
            session.setMenuLevel(1);
        } else {
            response = handleUserInput(session, text);
        }

        // Save session
        sessionService.saveSession(session);

        return response;
    }

    private String handleUserInput(UssdSession session, String text) {
        String[] inputs = text.split("\\*");
        String lastInput = inputs[inputs.length - 1];

        log.debug("Current menu: {}, Level: {}, Last input: {}",
                session.getCurrentMenu(), session.getMenuLevel(), lastInput);

        return switch (session.getCurrentMenu()) {
            case "MAIN_MENU" -> handleMainMenu(session, lastInput);
            case "SELECT_PROVINCE" -> handleProvinceSelection(session, lastInput);
            case "SELECT_SERVICE" -> handleServiceSelection(session, lastInput);
            case "SELECT_DATE" -> handleDateSelection(session, lastInput);
            case "SELECT_TIME" -> handleTimeSelection(session, lastInput);
            case "ENTER_NAME" -> handleNameInput(session, lastInput);
            case "ENTER_DOB" -> handleDobInput(session, lastInput);
            case "CONFIRM_BOOKING" -> handleBookingConfirmation(session, lastInput);
            case "LOOKUP_BOOKING" -> handleBookingLookup(session, lastInput);
            default -> "END Invalid menu state. Please try again.";
        };
    }

    private String showMainMenu() {
        return "CON Welcome to ZimID Booking\n" +
                "1. Book Appointment\n" +
                "2. Check My Booking\n" +
                "3. Help";
    }

    private String handleMainMenu(UssdSession session, String input) {
        return switch (input) {
            case "1" -> {
                session.setCurrentMenu("SELECT_PROVINCE");
                session.setMenuLevel(2);
                yield showProvinceMenu();
            }
            case "2" -> {
                session.setCurrentMenu("LOOKUP_BOOKING");
                session.setMenuLevel(2);
                yield "CON Enter your booking reference:";
            }
            case "3" -> "END Call 0242-795000 for assistance or visit www.rg.gov.zw";
            default -> "END Invalid option. Please try again.";
        };
    }

    private String showProvinceMenu() {
        try {
            List<ProvinceResponse> provinces = bookingServiceClient.getActiveProvinces();

            StringBuilder menu = new StringBuilder("CON Select Province:\n");
            for (int i = 0; i < provinces.size() && i < 10; i++) {
                menu.append(i + 1).append(". ").append(provinces.get(i).getName()).append("\n");
            }
            menu.append("0. Back");

            return menu.toString();
        } catch (Exception e) {
            log.error("Failed to fetch provinces", e);
            return "END Service temporarily unavailable. Please try again later.";
        }
    }

    private String handleProvinceSelection(UssdSession session, String input) {
        if ("0".equals(input)) {
            session.setCurrentMenu("MAIN_MENU");
            return showMainMenu();
        }

        try {
            int selection = Integer.parseInt(input);
            List<ProvinceResponse> provinces = bookingServiceClient.getActiveProvinces();

            if (selection < 1 || selection > provinces.size()) {
                return "END Invalid selection. Please try again.";
            }

            ProvinceResponse selectedProvince = provinces.get(selection - 1);
            session.setSelectedProvinceId(selectedProvince.getId());
            session.setSelectedProvinceName(selectedProvince.getName());
            session.setCurrentMenu("SELECT_SERVICE");
            session.setMenuLevel(3);

            return showServiceMenu();
        } catch (Exception e) {
            log.error("Error in province selection", e);
            return "END Invalid selection. Please try again.";
        }
    }

    private String showServiceMenu() {
        try {
            List<ServiceTypeResponse> services = bookingServiceClient.getActiveServices();

            StringBuilder menu = new StringBuilder("CON Select Service:\n");
            for (int i = 0; i < services.size(); i++) {
                ServiceTypeResponse service = services.get(i);
                menu.append(i + 1).append(". ")
                        .append(service.getName())
                        .append(" ($").append(service.getFee()).append(")\n");
            }
            menu.append("0. Back");

            return menu.toString();
        } catch (Exception e) {
            log.error("Failed to fetch services", e);
            return "END Service temporarily unavailable. Please try again later.";
        }
    }

    private String handleServiceSelection(UssdSession session, String input) {
        if ("0".equals(input)) {
            session.setCurrentMenu("SELECT_PROVINCE");
            return showProvinceMenu();
        }

        try {
            int selection = Integer.parseInt(input);
            List<ServiceTypeResponse> services = bookingServiceClient.getActiveServices();

            if (selection < 1 || selection > services.size()) {
                return "END Invalid selection. Please try again.";
            }

            ServiceTypeResponse selectedService = services.get(selection - 1);
            session.setSelectedServiceId(selectedService.getId());
            session.setSelectedServiceName(selectedService.getName());
            session.setCurrentMenu("SELECT_DATE");
            session.setMenuLevel(4);

            return showDateMenu();
        } catch (Exception e) {
            log.error("Error in service selection", e);
            return "END Invalid selection. Please try again.";
        }
    }

    private String showDateMenu() {
        LocalDate today = LocalDate.now();
        StringBuilder menu = new StringBuilder("CON Select Date:\n");

        for (int i = 1; i <= 7; i++) {
            LocalDate date = today.plusDays(i);
            menu.append(i).append(". ")
                    .append(date.format(DateTimeFormatter.ofPattern("EEE, dd MMM")))
                    .append("\n");
        }
        menu.append("8. Later dates\n");
        menu.append("0. Back");

        return menu.toString();
    }

    private String handleDateSelection(UssdSession session, String input) {
        if ("0".equals(input)) {
            session.setCurrentMenu("SELECT_SERVICE");
            return showServiceMenu();
        }

        try {
            int selection = Integer.parseInt(input);

            if (selection == 8) {
                return "CON Enter date (DD/MM/YYYY):\n0. Back";
            }

            if (selection < 1 || selection > 7) {
                return "END Invalid selection. Please try again.";
            }

            LocalDate selectedDate = LocalDate.now().plusDays(selection);
            session.setSelectedDate(selectedDate);
            session.setCurrentMenu("SELECT_TIME");
            session.setMenuLevel(5);

            return showTimeMenu(session);
        } catch (Exception e) {
            log.error("Error in date selection", e);
            return "END Invalid selection. Please try again.";
        }
    }

    private String showTimeMenu(UssdSession session) {
        try {
            List<LocalTime> availableSlots = bookingServiceClient.getAvailableSlots(
                    session.getSelectedProvinceId(),
                    session.getSelectedDate()
            );

            if (availableSlots.isEmpty()) {
                return "END No slots available for this date. Please try another date.";
            }

            StringBuilder menu = new StringBuilder("CON Select Time:\n");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

            for (int i = 0; i < Math.min(availableSlots.size(), 8); i++) {
                LocalTime slot = availableSlots.get(i);
                menu.append(i + 1).append(". ")
                        .append(slot.format(timeFormatter))
                        .append("\n");
            }
            menu.append("0. Back");

            return menu.toString();
        } catch (Exception e) {
            log.error("Failed to fetch time slots", e);
            return "END Service temporarily unavailable. Please try again later.";
        }
    }

    private String handleTimeSelection(UssdSession session, String input) {
        if ("0".equals(input)) {
            session.setCurrentMenu("SELECT_DATE");
            return showDateMenu();
        }

        try {
            int selection = Integer.parseInt(input);
            List<LocalTime> availableSlots = bookingServiceClient.getAvailableSlots(
                    session.getSelectedProvinceId(),
                    session.getSelectedDate()
            );

            if (selection < 1 || selection > availableSlots.size()) {
                return "END Invalid selection. Please try again.";
            }

            LocalTime selectedTime = availableSlots.get(selection - 1);
            session.setSelectedTime(selectedTime);
            session.setCurrentMenu("ENTER_NAME");
            session.setMenuLevel(6);

            return "CON Enter your full name:";
        } catch (Exception e) {
            log.error("Error in time selection", e);
            return "END Invalid selection. Please try again.";
        }
    }

    private String handleNameInput(UssdSession session, String input) {
        if (input.trim().length() < 2) {
            return "END Name too short. Please try again.";
        }

        session.setFullName(input.trim());
        session.setCurrentMenu("ENTER_DOB");
        session.setMenuLevel(7);

        return "CON Enter date of birth (DD/MM/YYYY):";
    }

    private String handleDobInput(UssdSession session, String input) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dob = LocalDate.parse(input, formatter);

            if (dob.isAfter(LocalDate.now())) {
                return "END Invalid date of birth. Please try again.";
            }

            session.setDateOfBirth(input);
            session.setCurrentMenu("CONFIRM_BOOKING");
            session.setMenuLevel(8);

            return showBookingConfirmation(session);
        } catch (Exception e) {
            return "END Invalid date format. Use DD/MM/YYYY.";
        }
    }

    private String showBookingConfirmation(UssdSession session) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        return "CON Confirm Booking:\n" +
                "Name: " + session.getFullName() + "\n" +
                "Office: " + session.getSelectedProvinceName() + "\n" +
                "Service: " + session.getSelectedServiceName() + "\n" +
                "Date: " + session.getSelectedDate().format(dateFormatter) + "\n" +
                "Time: " + session.getSelectedTime().format(timeFormatter) + "\n\n" +
                "1. Confirm\n" +
                "2. Cancel";
    }

    private String handleBookingConfirmation(UssdSession session, String input) {
        if ("2".equals(input)) {
            sessionService.deleteSession(session.getSessionId());
            return "END Booking cancelled.";
        }

        if (!"1".equals(input)) {
            return "END Invalid option.";
        }

        try {
            // Create booking via Feign client
            DateTimeFormatter dobFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dob = LocalDate.parse(session.getDateOfBirth(), dobFormatter);

            BookingRequest request = BookingRequest.builder()
                    .fullName(session.getFullName())
                    .dateOfBirth(dob)
                    .phoneNumber(session.getPhoneNumber())
                    .email(session.getPhoneNumber() + "@ussd.zimid.gov.zw") // Generate email
                    .provinceId(session.getSelectedProvinceId())
                    .serviceId(session.getSelectedServiceId())
                    .appointmentDate(session.getSelectedDate())
                    .appointmentTime(session.getSelectedTime())
                    .channel("USSD")
                    .notes("Created via USSD")
                    .build();

            BookingResponse booking = bookingServiceClient.createBooking(request);

            // Clear session
            sessionService.deleteSession(session.getSessionId());

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

            return "END Booking Confirmed!\n" +
                    "Ref: " + booking.getBookingReference() + "\n" +
                    "Office: " + session.getSelectedProvinceName() + "\n" +
                    "Date: " + session.getSelectedDate().format(dateFormatter) + "\n" +
                    "Time: " + session.getSelectedTime().format(timeFormatter) + "\n" +
                    "SMS confirmation sent.";
        } catch (Exception e) {
            log.error("Failed to create booking", e);
            return "END Booking failed. Please try again or call 0242-795000.";
        }
    }

    private String handleBookingLookup(UssdSession session, String input) {
        try {
            BookingResponse booking = bookingServiceClient.getBookingByReference(input.trim().toUpperCase());

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

            return "END Booking Found:\n" +
                    "Ref: " + booking.getBookingReference() + "\n" +
                    "Name: " + booking.getFullName() + "\n" +
                    "Office: " + booking.getProvinceName() + "\n" +
                    "Date: " + booking.getAppointmentDate().format(dateFormatter) + "\n" +
                    "Time: " + booking.getAppointmentTime().format(timeFormatter) + "\n" +
                    "Status: " + booking.getStatus();
        } catch (Exception e) {
            log.error("Booking lookup failed", e);
            return "END Booking not found. Please check the reference number.";
        }
    }
}
