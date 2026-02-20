package org.zimid.ussdservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTypeResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Integer durationMinutes;
    private BigDecimal fee;
    private String currency;
}