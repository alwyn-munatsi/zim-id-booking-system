package org.zimid.adminservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.zimid.adminservice.client.BookingServiceClient;
import org.zimid.adminservice.dto.BookingDTO;
import org.zimid.adminservice.security.AdminUserPrincipal;
import org.zimid.adminservice.service.AuditService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/bookings")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AdminBookingController {

    private final BookingServiceClient bookingServiceClient;
    private final AuditService auditService;

    /**
     * Search all bookings (with filters)
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'OPERATOR', 'VIEWER')")
    public ResponseEntity<List<BookingDTO>> searchBookings(
            @RequestParam String query,
            @AuthenticationPrincipal AdminUserPrincipal currentUser) {

        log.info("Admin {} searching bookings: {}", currentUser.getUsername(), query);
        List<BookingDTO> bookings = bookingServiceClient.searchBookings(query);

        // Log the search
        auditService.logAction(
                currentUser.getId(),
                currentUser.getUsername(),
                "SEARCH_BOOKINGS",
                "BOOKING",
                null,
                "Searched bookings with query: " + query,
                null
        );

        return ResponseEntity.ok(bookings);
    }

    /**
     * Update booking status
     */
    @PatchMapping("/{reference}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'OPERATOR')")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable String reference,
            @RequestParam String status,
            @AuthenticationPrincipal AdminUserPrincipal currentUser) {

        log.info("Admin {} updating booking {} to status {}",
                currentUser.getUsername(), reference, status);

        BookingDTO updated = bookingServiceClient.updateBookingStatus(reference, status);

        // Log the status update
        auditService.logAction(
                currentUser.getId(),
                currentUser.getUsername(),
                "UPDATE_BOOKING_STATUS",
                "BOOKING",
                reference,
                "Updated booking status to: " + status,
                null
        );

        return ResponseEntity.ok(updated);
    }

    /**
     * Cancel booking
     */
    @DeleteMapping("/{reference}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'OPERATOR')")
    public ResponseEntity<Map<String, String>> cancelBooking(
            @PathVariable String reference,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal AdminUserPrincipal currentUser) {

        log.info("Admin {} cancelling booking: {}", currentUser.getUsername(), reference);

        String cancellationReason = reason != null ? reason : "Cancelled by admin";
        bookingServiceClient.cancelBooking(reference, cancellationReason);

        // Log the cancellation
        auditService.logAction(
                currentUser.getId(),
                currentUser.getUsername(),
                "CANCEL_BOOKING",
                "BOOKING",
                reference,
                "Cancelled booking. Reason: " + cancellationReason,
                null
        );

        return ResponseEntity.ok(Map.of("message", "Booking cancelled successfully"));
    }

    /**
     * Bulk status update
     */
    @PatchMapping("/bulk/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Map<String, Object>> bulkUpdateStatus(
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal AdminUserPrincipal currentUser) {

        @SuppressWarnings("unchecked")
        List<String> references = (List<String>) request.get("references");
        String status = (String) request.get("status");

        log.info("Admin {} bulk updating {} bookings to status {}",
                currentUser.getUsername(), references.size(), status);

        int successCount = 0;
        int failCount = 0;

        for (String reference : references) {
            try {
                bookingServiceClient.updateBookingStatus(reference, status);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to update booking {}", reference, e);
                failCount++;
            }
        }

        // Log the bulk update
        auditService.logAction(
                currentUser.getId(),
                currentUser.getUsername(),
                "BULK_UPDATE_BOOKINGS",
                "BOOKING",
                null,
                String.format("Bulk updated %d bookings to status: %s (Failed: %d)",
                        successCount, status, failCount),
                null
        );

        return ResponseEntity.ok(Map.of(
                "success", successCount,
                "failed", failCount,
                "total", references.size()
        ));
    }
}
