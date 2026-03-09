package org.zimid.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvincePerformance {
    private String provinceName;
    private Long totalBookings;
    private BigDecimal revenue;
    private Integer capacity;
    private Double utilizationRate;
}
