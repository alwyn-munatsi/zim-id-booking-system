package org.zimid.bookingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "time_slots", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"startTime", "endTime"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false, length = 20)
    private String displayTime;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Integer slotOrder;
}
