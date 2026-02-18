package org.zimid.bookingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_reference", columnList = "bookingReference"),
        @Index(name = "idx_phone", columnList = "phoneNumber"),
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_appointment_date", columnList = "appointmentDate")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String bookingReference;

    @Column(nullable = false, length = 200)
    private String fullName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 200)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceType service;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private BookingChannel channel = BookingChannel.WEB;

    @Column(length = 1000)
    private String notes;

    @Column(length = 500)
    private String cancellationReason;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime confirmedAt;

    @Column
    private LocalDateTime completedAt;

    @Column
    private LocalDateTime cancelledAt;

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        NO_SHOW
    }

    public enum BookingChannel {
        WEB,
        MOBILE,
        USSD,
        ADMIN
    }
}
