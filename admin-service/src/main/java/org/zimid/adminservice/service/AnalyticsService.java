package org.zimid.adminservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.zimid.adminservice.client.BookingServiceClient;
import org.zimid.adminservice.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final JdbcTemplate jdbcTemplate;
    private final BookingServiceClient bookingServiceClient;

    public DashboardStats getDashboardStats() {
        log.info("Generating dashboard statistics");

        // Note: These queries connect to booking-service database
        // In production, you might want to replicate data or use events

        Long totalBookings = getTotalBookings();
        Long todayBookings = getTodayBookings();
        Long thisWeekBookings = getThisWeekBookings();
        Long thisMonthBookings = getThisMonthBookings();

        Map<String, Long> statusBreakdown = getBookingsByStatus();

        BigDecimal totalRevenue = calculateTotalRevenue();
        BigDecimal todayRevenue = calculateTodayRevenue();
        BigDecimal thisMonthRevenue = calculateMonthRevenue();

        Long totalUsers = getTotalUsers();
        Long activeUsers = getActiveUsers();
        Long newUsersToday = getNewUsersToday();

        Double avgBookingsPerDay = calculateAverageBookingsPerDay();
        Double capacityUtilization = calculateCapacityUtilization();

        List<DailyStats> last7Days = getLast7DaysStats();
        List<DailyStats> last30Days = getLast30DaysStats();

        Map<String, Long> bookingsByProvince = getBookingsByProvince();
        Map<String, Long> bookingsByService = getBookingsByService();
        Map<String, Long> bookingsByChannel = getBookingsByChannel();

        return DashboardStats.builder()
                .totalBookings(totalBookings)
                .todayBookings(todayBookings)
                .thisWeekBookings(thisWeekBookings)
                .thisMonthBookings(thisMonthBookings)
                .pendingBookings(statusBreakdown.getOrDefault("PENDING", 0L))
                .confirmedBookings(statusBreakdown.getOrDefault("CONFIRMED", 0L))
                .completedBookings(statusBreakdown.getOrDefault("COMPLETED", 0L))
                .cancelledBookings(statusBreakdown.getOrDefault("CANCELLED", 0L))
                .totalRevenue(totalRevenue)
                .todayRevenue(todayRevenue)
                .thisMonthRevenue(thisMonthRevenue)
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .newUsersToday(newUsersToday)
                .averageBookingsPerDay(avgBookingsPerDay)
                .capacityUtilization(capacityUtilization)
                .peakHour(getPeakHour())
                .mostPopularService(getMostPopularService())
                .busiestProvince(getBusiestProvince())
                .last7Days(last7Days)
                .last30Days(last30Days)
                .bookingsByProvince(bookingsByProvince)
                .bookingsByService(bookingsByService)
                .bookingsByStatus(statusBreakdown)
                .bookingsByChannel(bookingsByChannel)
                .build();
    }

    public AnalyticsReport generateReport(LocalDate startDate, LocalDate endDate, String reportType) {
        log.info("Generating analytics report from {} to {}", startDate, endDate);

        Long totalBookings = getBookingsInPeriod(startDate, endDate);
        Long completedBookings = getCompletedBookingsInPeriod(startDate, endDate);
        Long cancelledBookings = getCancelledBookingsInPeriod(startDate, endDate);

        BigDecimal totalRevenue = calculateRevenueInPeriod(startDate, endDate);

        Double completionRate = totalBookings > 0
                ? (completedBookings.doubleValue() / totalBookings) * 100
                : 0.0;

        Double cancellationRate = totalBookings > 0
                ? (cancelledBookings.doubleValue() / totalBookings) * 100
                : 0.0;

        List<DailyStats> timeSeriesData = getTimeSeriesData(startDate, endDate);
        List<ProvincePerformance> topProvinces = getProvincePerformance(startDate, endDate);
        List<ServicePerformance> topServices = getServicePerformance(startDate, endDate);

        Map<String, Long> bookingsByHour = getBookingsByHourInPeriod(startDate, endDate);
        Map<String, Long> bookingsByDayOfWeek = getBookingsByDayOfWeek(startDate, endDate);

        return AnalyticsReport.builder()
                .startDate(startDate)
                .endDate(endDate)
                .reportType(reportType)
                .totalBookings(totalBookings)
                .totalRevenue(totalRevenue)
                .completedBookings(completedBookings)
                .cancelledBookings(cancelledBookings)
                .completionRate(completionRate)
                .cancellationRate(cancellationRate)
                .timeSeriesData(timeSeriesData)
                .topProvinces(topProvinces)
                .topServices(topServices)
                .bookingsByHour(bookingsByHour)
                .bookingsByDayOfWeek(bookingsByDayOfWeek)
                .build();
    }

    // Helper methods for dashboard stats

    private Long getTotalBookings() {
        // Mock data - replace with actual database query
        return 1250L;
    }

    private Long getTodayBookings() {
        return 45L;
    }

    private Long getThisWeekBookings() {
        return 230L;
    }

    private Long getThisMonthBookings() {
        return 850L;
    }

    private Map<String, Long> getBookingsByStatus() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("PENDING", 25L);
        stats.put("CONFIRMED", 180L);
        stats.put("COMPLETED", 920L);
        stats.put("CANCELLED", 125L);
        return stats;
    }

    private BigDecimal calculateTotalRevenue() {
        return new BigDecimal("12500.00");
    }

    private BigDecimal calculateTodayRevenue() {
        return new BigDecimal("450.00");
    }

    private BigDecimal calculateMonthRevenue() {
        return new BigDecimal("8500.00");
    }

    private Long getTotalUsers() {
        return 3500L;
    }

    private Long getActiveUsers() {
        return 3200L;
    }

    private Long getNewUsersToday() {
        return 12L;
    }

    private Double calculateAverageBookingsPerDay() {
        return 42.5;
    }

    private Double calculateCapacityUtilization() {
        // (Total bookings / Total capacity) * 100
        return 65.5;
    }

    private String getPeakHour() {
        return "09:00 - 10:00";
    }

    private String getMostPopularService() {
        return "New National ID";
    }

    private String getBusiestProvince() {
        return "Harare";
    }

    private List<DailyStats> getLast7DaysStats() {
        List<DailyStats> stats = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            stats.add(DailyStats.builder()
                    .date(date)
                    .bookings((int) (30 + new Random().nextInt(20)))
                    .revenue(new BigDecimal(300 + new Random().nextInt(200)))
                    .newUsers((int) (3 + new Random().nextInt(5)))
                    .build());
        }
        return stats;
    }

    private List<DailyStats> getLast30DaysStats() {
        List<DailyStats> stats = new ArrayList<>();
        for (int i = 29; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            stats.add(DailyStats.builder()
                    .date(date)
                    .bookings((int) (25 + new Random().nextInt(30)))
                    .revenue(new BigDecimal(250 + new Random().nextInt(300)))
                    .newUsers((int) (2 + new Random().nextInt(8)))
                    .build());
        }
        return stats;
    }

    private Map<String, Long> getBookingsByProvince() {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("Harare", 450L);
        stats.put("Bulawayo", 280L);
        stats.put("Manicaland", 150L);
        stats.put("Mashonaland East", 120L);
        stats.put("Mashonaland West", 110L);
        stats.put("Midlands", 90L);
        stats.put("Masvingo", 50L);
        return stats;
    }

    private Map<String, Long> getBookingsByService() {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("New National ID", 620L);
        stats.put("ID Replacement", 380L);
        stats.put("ID Renewal", 220L);
        stats.put("Emergency ID", 30L);
        return stats;
    }

    private Map<String, Long> getBookingsByChannel() {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("WEB", 850L);
        stats.put("MOBILE", 280L);
        stats.put("USSD", 90L);
        stats.put("ADMIN", 30L);
        return stats;
    }

    // Helper methods for analytics reports

    private Long getBookingsInPeriod(LocalDate start, LocalDate end) {
        return 500L; // Mock
    }

    private Long getCompletedBookingsInPeriod(LocalDate start, LocalDate end) {
        return 420L; // Mock
    }

    private Long getCancelledBookingsInPeriod(LocalDate start, LocalDate end) {
        return 45L; // Mock
    }

    private BigDecimal calculateRevenueInPeriod(LocalDate start, LocalDate end) {
        return new BigDecimal("5000.00"); // Mock
    }

    private List<DailyStats> getTimeSeriesData(LocalDate start, LocalDate end) {
        return getLast30DaysStats(); // Mock
    }

    private List<ProvincePerformance> getProvincePerformance(LocalDate start, LocalDate end) {
        List<ProvincePerformance> performances = new ArrayList<>();
        performances.add(ProvincePerformance.builder()
                .provinceName("Harare")
                .totalBookings(200L)
                .revenue(new BigDecimal("2000.00"))
                .capacity(80)
                .utilizationRate(83.3)
                .build());
        performances.add(ProvincePerformance.builder()
                .provinceName("Bulawayo")
                .totalBookings(120L)
                .revenue(new BigDecimal("1200.00"))
                .capacity(60)
                .utilizationRate(66.7)
                .build());
        return performances;
    }

    private List<ServicePerformance> getServicePerformance(LocalDate start, LocalDate end) {
        List<ServicePerformance> performances = new ArrayList<>();
        performances.add(ServicePerformance.builder()
                .serviceName("New National ID")
                .totalBookings(250L)
                .revenue(new BigDecimal("1250.00"))
                .averagePrice(new BigDecimal("5.00"))
                .build());
        performances.add(ServicePerformance.builder()
                .serviceName("ID Replacement")
                .totalBookings(150L)
                .revenue(new BigDecimal("1500.00"))
                .averagePrice(new BigDecimal("10.00"))
                .build());
        return performances;
    }

    private Map<String, Long> getBookingsByHourInPeriod(LocalDate start, LocalDate end) {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("08:00", 45L);
        stats.put("09:00", 78L);
        stats.put("10:00", 92L);
        stats.put("11:00", 88L);
        stats.put("12:00", 65L);
        stats.put("14:00", 70L);
        stats.put("15:00", 52L);
        stats.put("16:00", 35L);
        return stats;
    }

    private Map<String, Long> getBookingsByDayOfWeek(LocalDate start, LocalDate end) {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("Monday", 95L);
        stats.put("Tuesday", 88L);
        stats.put("Wednesday", 102L);
        stats.put("Thursday", 85L);
        stats.put("Friday", 105L);
        return stats;
    }
}