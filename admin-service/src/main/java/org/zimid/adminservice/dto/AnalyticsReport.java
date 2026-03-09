package org.zimid.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsReport {

    private LocalDate startDate;
    private LocalDate endDate;
    private String reportType; // DAILY, WEEKLY, MONTHLY

    // Summary
    private Long totalBookings;
    private BigDecimal totalRevenue;
    private Long completedBookings;
    private Long cancelledBookings;
    private Double completionRate;
    private Double cancellationRate;

    // Time Series Data
    private List<DailyStats> timeSeriesData;

    // Top Performers
    private List<ProvincePerformance> topProvinces;
    private List<ServicePerformance> topServices;

    // Hour Distribution
    private Map<String, Long> bookingsByHour;

    // Day of Week Distribution
    private Map<String, Long> bookingsByDayOfWeek;
}