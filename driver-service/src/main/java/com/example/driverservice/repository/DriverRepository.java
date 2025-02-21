package com.example.driverservice.repository;

import com.example.driverservice.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findAllByDeletedFalse();
    Optional<Driver> findByIdAndDeletedFalse(Long id);
    Optional<Driver> findDriverByEmailAndDeletedFalse(String email);
    Optional<Driver> findDriverByPhoneNumberAndDeletedFalse(String email);
}
