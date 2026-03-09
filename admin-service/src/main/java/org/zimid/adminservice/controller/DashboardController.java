package org.zimid.adminservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zimid.adminservice.dto.AnalyticsReport;
import org.zimid.adminservice.dto.DashboardStats;
import org.zimid.adminservice.service.AnalyticsService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DashboardController {

    private final AnalyticsService analyticsService;

    /**
     * Get comprehensive dashboard statistics
     * Includes: bookings, revenue, users, trends, breakdowns
     */
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'OPERATOR', 'VIEWER')")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        log.info("Fetching dashboard statistics");
        DashboardStats stats = analyticsService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * Generate analytics report for a specific period
     *
     * @param startDate Start date (format: yyyy-MM-dd)
     * @param endDate End date (format: yyyy-MM-dd)
     * @param reportType DAILY, WEEKLY, or MONTHLY
     */
    @GetMapping("/report")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<AnalyticsReport> generateReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "DAILY") String reportType) {

        log.info("Generating {} report from {} to {}", reportType, startDate, endDate);
        AnalyticsReport report = analyticsService.generateReport(startDate, endDate, reportType);
        return ResponseEntity.ok(report);
    }

    /**
     * Get quick daily report (today's stats)
     */
    @GetMapping("/daily")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'OPERATOR', 'VIEWER')")
    public ResponseEntity<AnalyticsReport> getDailyReport() {
        LocalDate today = LocalDate.now();
        AnalyticsReport report = analyticsService.generateReport(today, today, "DAILY");
        return ResponseEntity.ok(report);
    }

    /**
     * Get weekly report (last 7 days)
     */
    @GetMapping("/weekly")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'OPERATOR', 'VIEWER')")
    public ResponseEntity<AnalyticsReport> getWeeklyReport() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        AnalyticsReport report = analyticsService.generateReport(startDate, endDate, "WEEKLY");
        return ResponseEntity.ok(report);
    }

    /**
     * Get monthly report (last 30 days)
     */
    @GetMapping("/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'OPERATOR', 'VIEWER')")
    public ResponseEntity<AnalyticsReport> getMonthlyReport() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        AnalyticsReport report = analyticsService.generateReport(startDate, endDate, "MONTHLY");
        return ResponseEntity.ok(report);
    }
}