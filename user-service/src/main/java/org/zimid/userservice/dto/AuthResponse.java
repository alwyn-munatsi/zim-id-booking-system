package org.zimid.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zimid.userservice.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long expiresIn; // in seconds
    private UserDTO user;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private Long id;
        private String fullName;
        private String email;
        private String phoneNumber;
        private User.UserRole role;
        private Boolean emailVerified;
        private Boolean phoneVerified;
        private LocalDateTime lastLoginAt;
    }
}