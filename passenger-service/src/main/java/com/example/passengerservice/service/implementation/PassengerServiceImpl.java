package com.example.passengerservice.service.implementation;
import com.example.passengerservice.dto.PassengerRequest;
import com.example.passengerservice.dto.PassengerResponse;
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
    public void createPassenger(PassengerRequest dto) {
        Passenger passenger = passengerMapper.toEntity(dto);
        passengerRepository.save(passenger);
    }

    @Override
    @Transactional
    public void updatePassenger(Long id, PassengerRequest dto) {
        Passenger passengerToUpdate = passengerRepository.findById(id).orElseThrow(); //TODO Add custom exception
        passengerToUpdate.setName(dto.getName());
        passengerToUpdate.setEmail(dto.getEmail());
        passengerToUpdate.setPhoneNumber(dto.getPhone());

        passengerRepository.save(passengerToUpdate);
    }

    @Override
    @Transactional
    public void deletePassengerById(Long id) {
        passengerRepository.deleteById(id);
    }

    @Override
    public PassengerResponse getPassengerById(Long id) {
        return passengerMapper.toDto(passengerRepository.findById(id).orElseThrow());
    }

    @Override
    public List<PassengerResponse> getAllPassengers() {
        return passengerRepository.findAll()
                .stream().map(passengerMapper::toDto)
                .collect(Collectors.toList());
    }
}
