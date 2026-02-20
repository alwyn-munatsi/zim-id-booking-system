package org.zimid.ussdservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private String fullName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private Long provinceId;
    private Long serviceId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String channel = "USSD";
    private String notes;
}