package org.zimid.bookingservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zimid.bookingservice.dto.ProvinceResponse;
import org.zimid.bookingservice.model.Province;
import org.zimid.bookingservice.repository.ProvinceRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/provinces")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProvinceController {

    private final ProvinceRepository provinceRepository;

    @GetMapping
    public ResponseEntity<List<ProvinceResponse>> getAllProvinces() {
        log.info("Getting all provinces");
        List<Province> provinces = provinceRepository.findAll();
        List<ProvinceResponse> response = provinces.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProvinceResponse>> getAllActiveProvinces() {
        log.info("Fetching active provinces");
        List<Province> provinces = provinceRepository.findByActiveTrue();
        List<ProvinceResponse> response = provinces.stream().map(this::mapToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProvinceResponse> getProvinceById(@PathVariable Long id) {
        log.info("Fetching province with id {}", id);
        Province province = provinceRepository.findById(id).orElseThrow(() -> new RuntimeException("Province not found"));
        return ResponseEntity.ok(mapToResponse(province));
    }

    private ProvinceResponse mapToResponse(Province province) {
        return ProvinceResponse.builder()
                .id(province.getId())
                .code(province.getCode())
                .name(province.getName())
                .officeName(province.getOfficeName())
                .address(province.getAddress())
                .phone(province.getPhone())
                .dailyCapacity(province.getDailyCapacity())
                .active(province.getActive())
                .latitude(province.getLatitude())
                .longitude(province.getLongitude())
                .build();
    }
}
