package org.zimid.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zimid.apigateway.filter.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // ========== USER SERVICE ROUTES ==========

                // Public routes (no authentication)
                .route("user-auth-register", r -> r
                        .path("/api/auth/register")
                        .uri("lb://USER-SERVICE"))

                .route("user-auth-login", r -> r
                        .path("/api/auth/login")
                        .uri("lb://USER-SERVICE"))

                // Protected user routes (authentication required)
                .route("user-protected", r -> r
                        .path("/api/users/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://USER-SERVICE"))

                // ========== BOOKING SERVICE ROUTES ==========

                // Public routes (no authentication)
                .route("provinces", r -> r
                        .path("/api/provinces/**")
                        .uri("lb://BOOKING-SERVICE"))

                .route("services", r -> r
                        .path("/api/services/**")
                        .uri("lb://BOOKING-SERVICE"))

                .route("slots-available", r -> r
                        .path("/api/bookings/slots/**")
                        .uri("lb://BOOKING-SERVICE"))

                // Protected booking routes (authentication required)
                .route("bookings-protected", r -> r
                        .path("/api/bookings/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://BOOKING-SERVICE"))

                // ========== ADMIN SERVICE ROUTES ==========

                // All admin routes require authentication
                .route("admin-protected", r -> r
                        .path("/api/admin/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://ADMIN-SERVICE"))

                // ========== FALLBACK ROUTE ==========

                .route("fallback", r -> r
                        .path("/**")
                        .uri("lb://BOOKING-SERVICE"))

                .build();
    }
}