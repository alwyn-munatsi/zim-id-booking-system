package org.zimid.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zimid.bookingservice.model.ServiceType;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {

    Optional<ServiceType> findByCode(String code);

    List<ServiceType> findByActiveTrue();

    Optional<ServiceType> findByCodeAndActiveTrue(String code);
}
