package org.zimid.bookingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    @Column(length = 10)
    private String currency = "USD";

    @ElementCollection
    @CollectionTable(name = "service_required_documents",
            joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "document")
    private List<String> requiredDocuments;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(length = 10)
    private String color;
}
