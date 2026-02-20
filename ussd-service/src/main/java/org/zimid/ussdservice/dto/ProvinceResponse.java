package org.zimid.ussdservice.dto;

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
}
