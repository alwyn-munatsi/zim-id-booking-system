package org.zimid.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zimid.bookingservice.model.Booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long id;
    private String bookingReference;
    private String fullName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private ProvinceDTO province;
    private ServiceTypeDTO service;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Booking.BookingStatus status;
    private Booking.BookingChannel channel;
    private String notes;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvinceDTO {
        private Long id;
        private String code;
        private String name;
        private String officeName;
        private String address;
        private String phone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceTypeDTO {
        private Long id;
        private String code;
        private String name;
        private String description;
        private Integer durationMinutes;
        private String fee;
        private String currency;
    }
}
