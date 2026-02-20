package org.zimid.bookingservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zimid.bookingservice.dto.BookingRequest;
import org.zimid.bookingservice.dto.BookingResponse;
import org.zimid.bookingservice.model.Booking;
import org.zimid.bookingservice.service.BookingService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request){
        log.info("Creating booking for phone: {}", request.getPhoneNumber());
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{reference}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable String reference){
        log.info("Fetching booking: {}", reference);
        BookingResponse response = bookingService.getBookingByReference(reference);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookingResponse>> searchBookings(@RequestParam String query){
        log.info("Searching bookings with query: {}", query);
        List<BookingResponse> bookings = bookingService.searchBookings(query);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<BookingResponse>> getBookingsByPhone(@PathVariable String phoneNumber){
        log.info("Fetching bookings for phone: {}", phoneNumber);
        List<BookingResponse> bookings = bookingService.getBookingsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<BookingResponse>> getBookingsByEmail(@PathVariable String email){
        log.info("Fetching bookings for email: {}", email);
        List<BookingResponse> bookings = bookingService.getBookingsByEmail(email);
        return ResponseEntity.ok(bookings);
    }

    @PatchMapping("/{reference}/status")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable String reference,
            @RequestParam Booking.BookingStatus status) {
        log.info("Updating booking {} status to {}", reference, status);
        BookingResponse response = bookingService.updateBookingStatus(reference, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reference}")
    public ResponseEntity<Map<String, String>> cancelBooking(@PathVariable String reference, @RequestParam(required = false) String reason){
        log.info("Cancelling booking {}", reference);
        bookingService.cancelBooking(reference, reason);
        return ResponseEntity.ok(Map.of("message", "Booking cancelled successfully"));
    }

    @GetMapping("/slots/available")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(
            @RequestParam Long provinceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        log.info("Fetching available slots for province {} on {}", provinceId, date);
        List<LocalTime> slots = bookingService.getAvailableTimeSlots(provinceId, date);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/slots/check")
    public ResponseEntity<Map<String, Boolean>> checkSlotAvailability(
            @RequestParam Long provinceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        boolean available = bookingService.isSlotAvailable(provinceId, date, time);
        return ResponseEntity.ok(Map.of("available", available));
    }
}
