package org.zimid.adminservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zimid.adminservice.dto.AdminAuthRequest;
import org.zimid.adminservice.dto.AdminAuthResponse;
import org.zimid.adminservice.service.AdminUserService;

@RestController
@RequestMapping("/api/v1/admin/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AdminAuthController {

    private final AdminUserService adminUserService;

    /**
     * Admin login endpoint
     * Returns JWT token for authenticated admin users
     */
    @PostMapping("/login")
    public ResponseEntity<AdminAuthResponse> login(@Valid @RequestBody AdminAuthRequest request) {
        log.info("Admin login request for username: {}", request.getUsername());
        AdminAuthResponse response = adminUserService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Check token validity
     */
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken() {
        return ResponseEntity.ok("Token is valid");
    }
}