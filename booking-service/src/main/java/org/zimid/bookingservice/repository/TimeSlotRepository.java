package org.zimid.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zimid.bookingservice.model.TimeSlot;

import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot,Long> {

    List<TimeSlot> findByActiveTrueOrderBySlotOrder();
}
