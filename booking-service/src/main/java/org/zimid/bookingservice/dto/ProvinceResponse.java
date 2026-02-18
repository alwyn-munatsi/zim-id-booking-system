package org.zimid.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceResponse {
    private Long id;
    private String code;
    private String name;
    private String officeName;
    private String address;
    private String phone;
    private Integer dailyCapacity;
    private Boolean active;
    private Double latitude;
    private Double longitude;
}
