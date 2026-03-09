package org.zimid.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {

    // Overall Statistics
    private Long totalBookings;
    private Long todayBookings;
    private Long thisWeekBookings;
    private Long thisMonthBookings;

    // Status Breakdown
    private Long pendingBookings;
    private Long confirmedBookings;
    private Long completedBookings;
    private Long cancelledBookings;

    // Revenue
    private BigDecimal totalRevenue;
    private BigDecimal todayRevenue;
    private BigDecimal thisMonthRevenue;

    // User Statistics
    private Long totalUsers;
    private Long activeUsers;
    private Long newUsersToday;

    // Performance Metrics
    private Double averageBookingsPerDay;
    private Double capacityUtilization; // Percentage
    private String peakHour;
    private String mostPopularService;
    private String busiestProvince;

    // Trends (for charts)
    private List<DailyStats> last7Days;
    private List<DailyStats> last30Days;

    // Breakdown by Province
    private Map<String, Long> bookingsByProvince;

    // Breakdown by Service
    private Map<String, Long> bookingsByService;

    // Breakdown by Status
    private Map<String, Long> bookingsByStatus;

    // Breakdown by Channel
    private Map<String, Long> bookingsByChannel;
}