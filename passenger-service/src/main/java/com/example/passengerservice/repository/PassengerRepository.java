package com.example.passengerservice.repository;

import com.example.passengerservice.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository  extends JpaRepository<Passenger, Long> {
}
