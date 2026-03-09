package org.zimid.adminservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zimid.adminservice.dto.AdminAuthRequest;
import org.zimid.adminservice.dto.AdminAuthResponse;
import org.zimid.adminservice.dto.AdminUserDTO;
import org.zimid.adminservice.dto.CreateAdminRequest;
import org.zimid.adminservice.model.AdminUser;
import org.zimid.adminservice.repository.AdminUserRepository;
import org.zimid.adminservice.security.JwtTokenProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuditService auditService;

    @Transactional
    public AdminAuthResponse login(AdminAuthRequest request) {
        log.info("Admin login attempt: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        AdminUser user = adminUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLastLoginAt(LocalDateTime.now());
        adminUserRepository.save(user);

        // Log the login
        auditService.logAction(user.getId(), user.getUsername(), "LOGIN", "ADMIN_USER",
                user.getId().toString(), "Admin user logged in", null);

        log.info("Admin logged in successfully: {}", user.getUsername());

        return AdminAuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getExpirationMs() / 1000)
                .user(mapToDTO(user))
                .build();
    }

    @Transactional
    public AdminUserDTO createAdminUser(CreateAdminRequest request, Long createdBy, String createdByUsername) {
        log.info("Creating new admin user: {}", request.getUsername());

        if (adminUserRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (adminUserRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        AdminUser user = AdminUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(request.getRole())
                .active(true)
                .build();

        user = adminUserRepository.save(user);

        // Log the creation
        auditService.logAction(createdBy, createdByUsername, "CREATE_ADMIN", "ADMIN_USER",
                user.getId().toString(),
                "Created new admin user: " + user.getUsername() + " with role: " + user.getRole(),
                null);

        log.info("Admin user created: {}", user.getUsername());

        return mapToDTO(user);
    }

    @Transactional(readOnly = true)
    public List<AdminUserDTO> getAllAdminUsers() {
        return adminUserRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminUserDTO getAdminUserById(Long id) {
        AdminUser user = adminUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));
        return mapToDTO(user);
    }

    @Transactional
    public AdminUserDTO updateAdminUser(Long id, CreateAdminRequest request, Long updatedBy, String updatedByUsername) {
        AdminUser user = adminUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user = adminUserRepository.save(user);

        // Log the update
        auditService.logAction(updatedBy, updatedByUsername, "UPDATE_ADMIN", "ADMIN_USER",
                user.getId().toString(),
                "Updated admin user: " + user.getUsername(),
                null);

        log.info("Admin user updated: {}", user.getUsername());

        return mapToDTO(user);
    }

    @Transactional
    public void deactivateAdminUser(Long id, Long deactivatedBy, String deactivatedByUsername) {
        AdminUser user = adminUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        user.setActive(false);
        adminUserRepository.save(user);

        // Log the deactivation
        auditService.logAction(deactivatedBy, deactivatedByUsername, "DEACTIVATE_ADMIN", "ADMIN_USER",
                user.getId().toString(),
                "Deactivated admin user: " + user.getUsername(),
                null);

        log.info("Admin user deactivated: {}", user.getUsername());
    }

    @Transactional
    public void activateAdminUser(Long id, Long activatedBy, String activatedByUsername) {
        AdminUser user = adminUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        user.setActive(true);
        adminUserRepository.save(user);

        // Log the activation
        auditService.logAction(activatedBy, activatedByUsername, "ACTIVATE_ADMIN", "ADMIN_USER",
                user.getId().toString(),
                "Activated admin user: " + user.getUsername(),
                null);

        log.info("Admin user activated: {}", user.getUsername());
    }

    private AdminUserDTO mapToDTO(AdminUser user) {
        return AdminUserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}