package org.zimid.bookingservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zimid.bookingservice.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 200, message = "Full name must be between 2 and 200 characters")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+263[0-9]{9}$", message = "Invalid Zimbabwean phone number format. Use +263XXXXXXXXX")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Province ID is required")
    private Long provinceId;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    private LocalTime appointmentTime;

    private Booking.BookingChannel channel = Booking.BookingChannel.WEB;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}
