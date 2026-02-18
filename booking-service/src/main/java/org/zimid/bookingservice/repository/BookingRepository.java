package org.zimid.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zimid.bookingservice.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingReference(String bookingReference);

    List<Booking> findByPhoneNumber(String phoneNumber);
    List<Booking> findByEmail(String email);

    @Query("SELECT b FROM Booking b WHERE " +
            "LOWER(b.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "b.phoneNumber LIKE CONCAT('%', :searchTerm, '%') OR " +
            "b.email LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "b.bookingReference LIKE UPPER(CONCAT('%', :searchTerm, '%'))")
    List<Booking> searchBookings(@Param("searchTerm") String searchTerm);

    List<Booking> findByProvinceIdAndStatus(Long provinceId, Booking.BookingStatus status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE " +
            "b.province.id = :provinceId AND " +
            "b.appointmentDate = :date AND " +
            "b.status NOT IN ('CANCELLED', 'NO_SHOW')")
    Long countBookingsForProvinceOnDate(
            @Param("provinceId") Long provinceId,
            @Param("date") LocalDate date
    );

    @Query("SELECT COUNT(b) FROM Booking b WHERE " +
            "b.province.id = :provinceId AND " +
            "b.appointmentDate = :date AND " +
            "b.appointmentTime = :time AND " +
            "b.status NOT IN ('CANCELLED', 'NO_SHOW')")
    Long countBookingsForTimeSlot(
            @Param("provinceId") Long provinceId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time
    );

    @Query("SELECT b FROM Booking b WHERE " +
            "b.appointmentDate BETWEEN :startDate AND :endDate AND " +
            "b.status = :status")
    List<Booking> findBookingsByDateRangeAndStatus(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") Booking.BookingStatus status
    );

    @Query("SELECT b FROM Booking b WHERE " +
            "b.province.id = :provinceId AND " +
            "b.appointmentDate BETWEEN :startDate AND :endDate")
    List<Booking> findByProvinceAndDateRange(
            @Param("provinceId") Long provinceId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
