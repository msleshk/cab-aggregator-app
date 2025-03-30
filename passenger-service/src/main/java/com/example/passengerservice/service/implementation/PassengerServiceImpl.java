package com.example.passengerservice.service.implementation;

import com.example.passengerservice.dto.kafka.NewPassengerBalance;
import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.dto.response.PassengerResponse;
import com.example.passengerservice.dto.response.PassengerResponseList;
import com.example.passengerservice.exception.EmailAlreadyTakenException;
import com.example.passengerservice.exception.PassengerNotFoundException;
import com.example.passengerservice.exception.PhoneNumberAlreadyTakenException;
import com.example.passengerservice.kafka.producer.KafkaProducer;
import com.example.passengerservice.model.Passenger;
import com.example.passengerservice.repository.PassengerRepository;
import com.example.passengerservice.service.PassengerService;
import com.example.passengerservice.util.PassengerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    private final KafkaProducer kafkaProducer;

    @Override
    @Transactional
    public void addPassenger(PassengerRequest dto) {
        checkIfEmailUnique(dto.email());
        checkIfPhoneUnique(dto.phoneNumber());

        Passenger passenger = passengerMapper.toEntity(dto);

        passengerRepository.save(passenger);
//        kafkaProducer.sendNewPassengerBalance(NewPassengerBalance.builder()
//                .id(passenger.getId())
//                .build());
    }

    @Override
    @Transactional
    public void updatePassenger(Long id, PassengerRequest dto) {
        Passenger passengerToUpdate = findPassengerById(id);

        updateIfChanged(passengerToUpdate, dto);

        passengerToUpdate.setName(dto.name());

        passengerRepository.save(passengerToUpdate);
    }


    @Override
    @Transactional
    public void deletePassengerById(Long id) {
        Passenger passenger = findPassengerById(id);
        passenger.setDeleted(true);
        passengerRepository.save(passenger);
    }

    @Override
    public PassengerResponse getPassengerById(Long id) {
        return passengerMapper.toDto(findPassengerById(id));
    }

    @Override
    public PassengerResponseList getAllPassengers(int offset, int limit) {
        Page<Passenger> passengerPage = passengerRepository.findAllByDeletedFalse(PageRequest.of(offset, limit));

        return new PassengerResponseList(passengerPage.getContent().stream()
                .map(passengerMapper::toDto)
                .collect(Collectors.toList()));
    }

    private void updateIfChanged(Passenger passengerToUpdate, PassengerRequest dto){
        if (!passengerToUpdate.getEmail().equals(dto.email())) {
            checkIfEmailUnique(dto.email());
            passengerToUpdate.setEmail(dto.email());
        }

        if (!passengerToUpdate.getPhoneNumber().equals(dto.phoneNumber())) {
            checkIfPhoneUnique(dto.phoneNumber());
            passengerToUpdate.setPhoneNumber(dto.phoneNumber());
        }
    }

    private Passenger findPassengerById(Long id){
        return passengerRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new PassengerNotFoundException("No such passenger!"));
    }

    private void checkIfPhoneUnique(String phoneNumber){
        if (passengerRepository.findPassengerByPhoneNumberAndDeletedFalse(phoneNumber).isPresent()) {
            throw new PhoneNumberAlreadyTakenException("Passenger with this phone number already exist!");
        }
    }

    private void checkIfEmailUnique(String email){
        if (passengerRepository.findPassengerByEmailAndDeletedFalse(email).isPresent()) {
            throw new EmailAlreadyTakenException("Email already been taken!");
        }
    }
}
