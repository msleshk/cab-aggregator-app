package com.example.passengerservice.repository;

import com.example.passengerservice.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByIdAndDeletedFalse(Long id);

    List<Passenger> findAllByDeletedFalse();

    Optional<Passenger> findPassengerByEmailAndDeletedFalse(String email);

    Optional<Passenger> findPassengerByPhoneNumberAndDeletedFalse(String phone);
}
