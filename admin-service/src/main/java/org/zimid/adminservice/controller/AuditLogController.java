package org.zimid.adminservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zimid.adminservice.dto.AuditLogDTO;
import org.zimid.adminservice.model.AuditLog;
import org.zimid.adminservice.service.AuditService;

@RestController
@RequestMapping("/api/v1/admin/audit-logs")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuditLogController {

    private final AuditService auditService;

    /**
     * Get all audit logs (paginated)
     * Only SUPER_ADMIN and ADMIN can view audit logs
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<AuditLogDTO>> getAllAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("Fetching audit logs - page: {}, size: {}", page, size);

        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AuditLog> auditLogs = auditService.getAuditLogs(pageable);

        Page<AuditLogDTO> response = auditLogs.map(this::mapToDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Get audit logs by admin user
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Fetching audit logs for user: {}", userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AuditLog> auditLogs = auditService.getAuditLogsByUser(userId, pageable);

        Page<AuditLogDTO> response = auditLogs.map(this::mapToDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Get audit logs by action type
     */
    @GetMapping("/action/{action}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogsByAction(
            @PathVariable String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Fetching audit logs for action: {}", action);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AuditLog> auditLogs = auditService.getAuditLogsByAction(action, pageable);

        Page<AuditLogDTO> response = auditLogs.map(this::mapToDTO);
        return ResponseEntity.ok(response);
    }

    private AuditLogDTO mapToDTO(AuditLog log) {
        return AuditLogDTO.builder()
                .id(log.getId())
                .adminUserId(log.getAdminUserId())
                .adminUsername(log.getAdminUsername())
                .action(log.getAction())
                .entity(log.getEntity())
                .entityId(log.getEntityId())
                .details(log.getDetails())
                .ipAddress(log.getIpAddress())
                .createdAt(log.getCreatedAt())
                .build();
    }
}