package com.example.passengerservice.service.implementation;

import com.example.passengerservice.dto.PassengerRequest;
import com.example.passengerservice.dto.PassengerResponse;
import com.example.passengerservice.exception.EmailAlreadyTakenException;
import com.example.passengerservice.exception.PassengerNotFoundException;
import com.example.passengerservice.exception.PhoneNumberAlreadyTakenException;
import com.example.passengerservice.model.Passenger;
import com.example.passengerservice.repository.PassengerRepository;
import com.example.passengerservice.service.PassengerService;
import com.example.passengerservice.util.PassengerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    public PassengerServiceImpl(PassengerRepository passengerRepository, PassengerMapper passengerMapper) {
        this.passengerRepository = passengerRepository;
        this.passengerMapper = passengerMapper;
    }

    @Override
    @Transactional
    public void addPassenger(PassengerRequest dto) {
        if (passengerRepository.findPassengerByEmailAndDeletedFalse(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException("Email already been taken!");
        } else if (passengerRepository.findPassengerByPhoneNumberAndDeletedFalse(dto.getPhoneNumber()).isPresent()) {
            throw new PhoneNumberAlreadyTakenException("Passenger with this phone number already exist!");
        }

        Passenger passenger = passengerMapper.toEntity(dto);

        passengerRepository.save(passenger);
    }

    @Override
    @Transactional
    public void updatePassenger(Long id, PassengerRequest dto) {
        Passenger passengerToUpdate = passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException("No such passenger!"));

        if (!passengerToUpdate.getEmail().equals(dto.getEmail())) {
            if (passengerRepository.findPassengerByEmailAndDeletedFalse(dto.getEmail()).isPresent()) {
                throw new EmailAlreadyTakenException("Email already been taken!");
            }
            passengerToUpdate.setEmail(dto.getEmail());
        }

        if (!passengerToUpdate.getPhoneNumber().equals(dto.getPhoneNumber())) {
            if (passengerRepository.findPassengerByPhoneNumberAndDeletedFalse(dto.getPhoneNumber()).isPresent()) {
                throw new PhoneNumberAlreadyTakenException("Passenger with this phone number already exists!");
            }
            passengerToUpdate.setPhoneNumber(dto.getPhoneNumber());
        }

        passengerToUpdate.setName(dto.getName());

        passengerRepository.save(passengerToUpdate);
    }


    @Override
    @Transactional
    public void deletePassengerById(Long id) {
        Passenger passenger = passengerRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new PassengerNotFoundException("No such passenger!"));
        passenger.setDeleted(true);
        passengerRepository.save(passenger);
    }

    @Override
    public PassengerResponse getPassengerById(Long id) {
        return passengerMapper.toDto(passengerRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new PassengerNotFoundException("No such passenger!")));
    }

    @Override
    public List<PassengerResponse> getAllPassengers() {
        return passengerRepository.findAllByDeletedFalse()
                .stream().map(passengerMapper::toDto)
                .collect(Collectors.toList());
    }
}
