package org.zimid.ussdservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.zimid.ussdservice.dto.BookingRequest;
import org.zimid.ussdservice.dto.BookingResponse;
import org.zimid.ussdservice.dto.ProvinceResponse;
import org.zimid.ussdservice.dto.ServiceTypeResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingServiceClient {

    @GetMapping("/api/v1/provinces/active")
    List<ProvinceResponse> getActiveProvinces();

    @GetMapping("/api/v1/services/active")
    List<ServiceTypeResponse> getActiveServices();

    @GetMapping("/api/v1/bookings/slots/available")
    List<LocalTime> getAvailableSlots(
            @RequestParam Long provinceId,
            @RequestParam LocalDate date
    );

    @PostMapping("/api/v1/bookings")
    BookingResponse createBooking(@RequestBody BookingRequest request);

    @GetMapping("/api/v1/bookings/{reference}")
    BookingResponse getBookingByReference(@PathVariable String reference);
}
