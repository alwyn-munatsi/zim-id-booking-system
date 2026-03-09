package org.zimid.adminservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zimid.adminservice.client.BookingServiceClient;
import org.zimid.adminservice.dto.ProvinceDTO;
import org.zimid.adminservice.dto.ServiceTypeDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/config")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ConfigurationController {

    private final BookingServiceClient bookingServiceClient;

    /**
     * Get all provinces (for configuration)
     */
    @GetMapping("/provinces")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'VIEWER')")
    public ResponseEntity<List<ProvinceDTO>> getAllProvinces() {
        log.info("Admin fetching all provinces");
        List<ProvinceDTO> provinces = bookingServiceClient.getAllProvinces();
        return ResponseEntity.ok(provinces);
    }

    /**
     * Get all services (for configuration)
     */
    @GetMapping("/services")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'VIEWER')")
    public ResponseEntity<List<ServiceTypeDTO>> getAllServices() {
        log.info("Admin fetching all services");
        List<ServiceTypeDTO> services = bookingServiceClient.getAllServices();
        return ResponseEntity.ok(services);
    }
}