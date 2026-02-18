package org.zimid.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zimid.bookingservice.model.Province;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    Optional<Province> findByCode(String code);

    List<Province> findByActiveTrue();

    Optional<Province> findByCodeAndActiveTrue(String code);

}
