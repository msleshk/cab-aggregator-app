package com.example.driverservice.repository;

import com.example.driverservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findCarByCarNumberAndDeletedFalse(String carNumber);
    Optional<Car> findCarByIdAndDeletedFalse(Long id);
    List<Car> findAllByDeletedFalse();
}
