package org.zimid.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.zimid.userservice.dto.RegisterRequest;
import org.zimid.userservice.dto.UserResponse;
import org.zimid.userservice.security.UserPrincipal;
import org.zimid.userservice.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        log.info("Get current user profile: {}", currentUser.getId());
        UserResponse response = userService.getCurrentUser(currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("Get user by id: {}", id);
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("Get user by email: {}", email);
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody RegisterRequest request) {
        log.info("Update profile for user: {}", currentUser.getId());
        UserResponse response = userService.updateProfile(currentUser.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestBody Map<String, String> passwordData) {
        log.info("Change password for user: {}", currentUser.getId());

        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");

        userService.changePassword(currentUser.getId(), oldPassword, newPassword);

        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Map<String, String>> deleteAccount(@AuthenticationPrincipal UserPrincipal currentUser) {
        log.info("Delete account for user: {}", currentUser.getId());
        userService.deleteAccount(currentUser.getId());
        return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
    }
}