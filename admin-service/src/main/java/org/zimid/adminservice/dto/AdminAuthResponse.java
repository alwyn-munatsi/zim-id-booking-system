package org.zimid.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long expiresIn; // in seconds
    private AdminUserDTO user;
}