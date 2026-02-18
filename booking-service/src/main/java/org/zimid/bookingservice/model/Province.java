package org.zimid.bookingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "provinces")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String officeName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private Integer dailyCapacity;

    @Column(nullable = false)
    private Boolean active = true;

    private Double latitude;
    private Double longitude;
}
