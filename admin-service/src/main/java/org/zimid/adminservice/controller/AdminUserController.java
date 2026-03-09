package org.zimid.adminservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.zimid.adminservice.dto.AdminUserDTO;
import org.zimid.adminservice.dto.CreateAdminRequest;
import org.zimid.adminservice.security.AdminUserPrincipal;
import org.zimid.adminservice.service.AdminUserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * Get all admin users
     * Only SUPER_ADMIN can view all users
     */
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<AdminUserDTO>> getAllAdminUsers() {
        log.info("Fetching all admin users");
        List<AdminUserDTO> users = adminUserService.getAllAdminUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get current admin user profile
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'OPERATOR', 'VIEWER')")
    public ResponseEntity<AdminUserDTO> getCurrentUser(@AuthenticationPrincipal AdminUserPrincipal currentUser) {
        log.info("Fetching current admin user: {}", currentUser.getUsername());
        AdminUserDTO user = adminUserService.getAdminUserById(currentUser.getId());
        return ResponseEntity.ok(user);
    }

    /**
     * Get admin user by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AdminUserDTO> getAdminUserById(@PathVariable Long id) {
        log.info("Fetching admin user with id: {}", id);
        AdminUserDTO user = adminUserService.getAdminUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Create new admin user
     * Only SUPER_ADMIN can create new admins
     */
    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AdminUserDTO> createAdminUser(
            @Valid @RequestBody CreateAdminRequest request,
            @AuthenticationPrincipal AdminUserPrincipal currentUser) {

        log.info("Creating new admin user: {} by {}", request.getUsername(), currentUser.getUsername());
        AdminUserDTO user = adminUserService.createAdminUser(
                request,
                currentUser.getId(),
                currentUser.getUsername()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Update admin user
     * Only SUPER_ADMIN can update users
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AdminUserDTO> updateAdminUser(
            @PathVariable Long id,
            @Valid @RequestBody CreateAdminRequest request,
            @AuthenticationPrincipal AdminUserPrincipal currentUser) {

        log.info("Updating admin user: {} by {}", id, currentUser.getUsername());
        AdminUserDTO user = adminUserService.updateAdminUser(
                id,
                request,
                currentUser.getId(),
                currentUser.getUsername()
        );
        return ResponseEntity.ok(user);
    }

    /**
     * Deactivate admin user
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> deactivateAdminUser(
            @PathVariable Long id,
            @AuthenticationPrincipal AdminUserPrincipal currentUser) {

        log.info("Deactivating admin user: {} by {}", id, currentUser.getUsername());
        adminUserService.deactivateAdminUser(id, currentUser.getId(), currentUser.getUsername());
        return ResponseEntity.ok(Map.of("message", "Admin user deactivated successfully"));
    }

    /**
     * Activate admin user
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> activateAdminUser(
            @PathVariable Long id,
            @AuthenticationPrincipal AdminUserPrincipal currentUser) {

        log.info("Activating admin user: {} by {}", id, currentUser.getUsername());
        adminUserService.activateAdminUser(id, currentUser.getId(), currentUser.getUsername());
        return ResponseEntity.ok(Map.of("message", "Admin user activated successfully"));
    }
}