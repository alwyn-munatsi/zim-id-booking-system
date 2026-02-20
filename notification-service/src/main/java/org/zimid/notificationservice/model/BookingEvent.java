package org.zimid.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bookingReference;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String provinceName;
    private String serviceName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private String eventType; // CREATED, UPDATED, CANCELLED
    private LocalDateTime timestamp;
}
