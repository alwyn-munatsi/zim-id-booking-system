package org.zimid.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {

    private Long id;
    private Long adminUserId;
    private String adminUsername;
    private String action;
    private String entity;
    private String entityId;
    private String details;
    private String ipAddress;
    private LocalDateTime createdAt;
}