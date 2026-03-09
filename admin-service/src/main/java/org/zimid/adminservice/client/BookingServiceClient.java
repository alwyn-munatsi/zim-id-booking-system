package org.zimid.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.zimid.adminservice.dto.BookingDTO;
import org.zimid.adminservice.dto.ProvinceDTO;
import org.zimid.adminservice.dto.ServiceTypeDTO;

import java.util.List;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingServiceClient {

    @GetMapping("/api/v1/provinces")
    List<ProvinceDTO> getAllProvinces();

    @GetMapping("/api/v1/services")
    List<ServiceTypeDTO> getAllServices();

    @GetMapping("/api/v1/bookings/search")
    List<BookingDTO> searchBookings(@RequestParam String query);

    @PatchMapping("/api/v1/bookings/{reference}/status")
    BookingDTO updateBookingStatus(
            @PathVariable String reference,
            @RequestParam String status
    );

    @DeleteMapping("/api/v1/bookings/{reference}")
    void cancelBooking(
            @PathVariable String reference,
            @RequestParam(required = false) String reason
    );
}