package org.zimid.ussdservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UssdSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sessionId;
    private String phoneNumber;
    private String currentMenu;
    private int menuLevel;

    // Booking data
    private Long selectedProvinceId;
    private String selectedProvinceName;
    private Long selectedServiceId;
    private String selectedServiceName;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private String fullName;
    private String dateOfBirth;

    // For lookup
    private String lookupReference;
}