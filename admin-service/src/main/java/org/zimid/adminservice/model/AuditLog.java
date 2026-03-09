package org.zimid.adminservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_admin_user_id", columnList = "adminUserId"),
        @Index(name = "idx_action", columnList = "action"),
        @Index(name = "idx_created_at", columnList = "createdAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long adminUserId;

    @Column(nullable = false)
    private String adminUsername;

    @Column(nullable = false)
    private String action; // LOGIN, UPDATE_BOOKING, CANCEL_BOOKING, etc.

    @Column(nullable = false)
    private String entity; // BOOKING, USER, PROVINCE, etc.

    private String entityId; // Booking reference, user ID, etc.

    @Column(columnDefinition = "TEXT")
    private String details; // JSON or description of changes

    private String ipAddress;

    private String userAgent;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}