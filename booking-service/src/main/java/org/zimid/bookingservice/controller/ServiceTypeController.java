package org.zimid.bookingservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zimid.bookingservice.dto.ServiceTypeResponse;
import org.zimid.bookingservice.model.ServiceType;
import org.zimid.bookingservice.repository.ServiceTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ServiceTypeController {

    private final ServiceTypeRepository serviceTypeRepository;

    @GetMapping
    public ResponseEntity<List<ServiceTypeResponse>> getAllServices() {
        log.info("Fetching all services");
        List<ServiceType> services = serviceTypeRepository.findAll();
        List<ServiceTypeResponse> response = services.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ServiceTypeResponse>> getActiveServices() {
        log.info("Fetching active services");
        List<ServiceType> services = serviceTypeRepository.findByActiveTrue();
        List<ServiceTypeResponse> response = services.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTypeResponse> getServiceById(@PathVariable Long id) {
        log.info("Fetching service with id: {}", id);
        ServiceType service = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        return ResponseEntity.ok(mapToResponse(service));
    }

    private ServiceTypeResponse mapToResponse(ServiceType service) {
        return ServiceTypeResponse.builder()
                .id(service.getId())
                .code(service.getCode())
                .name(service.getName())
                .description(service.getDescription())
                .durationMinutes(service.getDurationMinutes())
                .fee(service.getFee())
                .currency(service.getCurrency())
                .requiredDocuments(service.getRequiredDocuments())
                .active(service.getActive())
                .color(service.getColor())
                .build();
    }
}
