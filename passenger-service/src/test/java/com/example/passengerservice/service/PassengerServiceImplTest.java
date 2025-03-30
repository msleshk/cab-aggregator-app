package com.example.passengerservice.service;

import com.example.passengerservice.dto.kafka.NewPassengerBalance;
import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.dto.response.PassengerResponse;
import com.example.passengerservice.exception.EmailAlreadyTakenException;
import com.example.passengerservice.exception.PassengerNotFoundException;
import com.example.passengerservice.exception.PhoneNumberAlreadyTakenException;
import com.example.passengerservice.kafka.producer.KafkaProducer;
import com.example.passengerservice.model.Passenger;
import com.example.passengerservice.repository.PassengerRepository;
import com.example.passengerservice.service.implementation.PassengerServiceImpl;
import com.example.passengerservice.util.PassengerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.passengerservice.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private PassengerMapper passengerMapper;
    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    private Passenger passenger;
    private PassengerRequest passengerRequest;
    private PassengerResponse passengerResponse;

    @BeforeEach
    void setUp() {
        passenger = new Passenger();
        passenger.setId(PASSENGER_ID);
        passenger.setName(PASSENGER_NAME);
        passenger.setEmail(PASSENGER_EMAIL);
        passenger.setPhoneNumber(PASSENGER_PHONE);

        passengerRequest = new PassengerRequest(
                PASSENGER_NAME,
                PASSENGER_EMAIL,
                PASSENGER_PHONE
        );

        passengerResponse = new PassengerResponse(
                PASSENGER_ID,
                PASSENGER_NAME,
                PASSENGER_EMAIL,
                PASSENGER_PHONE,
                AVERAGE_RATING
        );
    }

    @Test
    void shouldReturnPassengerById() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(passenger));
        when(passengerMapper.toDto(passenger)).thenReturn(passengerResponse);

        PassengerResponse response = passengerService.getPassengerById(PASSENGER_ID);

        assertNotNull(response);
        assertEquals(PASSENGER_ID, response.id());
    }

    @Test
    void shouldThrowExceptionWhenPassengerNotFound() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(PassengerNotFoundException.class, () ->
                passengerService.getPassengerById(PASSENGER_ID));

        assertEquals(EXCEPTION_MESSAGE_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldAddPassengerSuccessfully() {
        when(passengerRepository.findPassengerByEmailAndDeletedFalse(PASSENGER_EMAIL))
                .thenReturn(Optional.empty());
        when(passengerRepository.findPassengerByPhoneNumberAndDeletedFalse(PASSENGER_PHONE))
                .thenReturn(Optional.empty());
        when(passengerMapper.toEntity(passengerRequest)).thenReturn(passenger);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        passengerService.addPassenger(passengerRequest);

        verify(passengerRepository, times(1)).save(any(Passenger.class));
        verify(kafkaProducer, times(1)).sendNewPassengerBalance(any(NewPassengerBalance.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingPassengerWithDuplicateEmail() {
        when(passengerRepository.findPassengerByEmailAndDeletedFalse(PASSENGER_EMAIL))
                .thenReturn(Optional.of(passenger));

        Exception exception = assertThrows(EmailAlreadyTakenException.class, () ->
                passengerService.addPassenger(passengerRequest));

        assertEquals(EXCEPTION_MESSAGE_EMAIL_TAKEN, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddingPassengerWithDuplicatePhone() {
        when(passengerRepository.findPassengerByEmailAndDeletedFalse(PASSENGER_EMAIL))
                .thenReturn(Optional.empty());
        when(passengerRepository.findPassengerByPhoneNumberAndDeletedFalse(PASSENGER_PHONE))
                .thenReturn(Optional.of(passenger));

        Exception exception = assertThrows(PhoneNumberAlreadyTakenException.class, () ->
                passengerService.addPassenger(passengerRequest));

        assertEquals(EXCEPTION_MESSAGE_PHONE_TAKEN, exception.getMessage());
    }

    @Test
    void shouldUpdatePassengerSuccessfully() {
        PassengerRequest updateRequest = new PassengerRequest(
                NEW_PASSENGER_NAME,
                NEW_PASSENGER_EMAIL,
                NEW_PASSENGER_PHONE
        );

        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(passenger));
        when(passengerRepository.findPassengerByEmailAndDeletedFalse(NEW_PASSENGER_EMAIL))
                .thenReturn(Optional.empty());
        when(passengerRepository.findPassengerByPhoneNumberAndDeletedFalse(NEW_PASSENGER_PHONE))
                .thenReturn(Optional.empty());

        passengerService.updatePassenger(PASSENGER_ID, updateRequest);

        assertEquals(NEW_PASSENGER_NAME, passenger.getName());
        assertEquals(NEW_PASSENGER_EMAIL, passenger.getEmail());
        assertEquals(NEW_PASSENGER_PHONE, passenger.getPhoneNumber());

        verify(passengerRepository, times(1)).save(passenger);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithDuplicateEmail() {
        PassengerRequest updateRequest = new PassengerRequest(
                NEW_PASSENGER_NAME,
                NEW_PASSENGER_EMAIL,
                PASSENGER_PHONE
        );

        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(passenger));
        when(passengerRepository.findPassengerByEmailAndDeletedFalse(NEW_PASSENGER_EMAIL))
                .thenReturn(Optional.of(new Passenger()));

        assertThrows(EmailAlreadyTakenException.class, () ->
                passengerService.updatePassenger(PASSENGER_ID, updateRequest));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithDuplicatePhone() {
        PassengerRequest updateRequest = new PassengerRequest(
                NEW_PASSENGER_NAME,
                PASSENGER_EMAIL,
                NEW_PASSENGER_PHONE
        );

        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(passenger));
        when(passengerRepository.findPassengerByPhoneNumberAndDeletedFalse(NEW_PASSENGER_PHONE))
                .thenReturn(Optional.of(new Passenger()));

        assertThrows(PhoneNumberAlreadyTakenException.class, () ->
                passengerService.updatePassenger(PASSENGER_ID, updateRequest));
    }

    @Test
    void shouldDeletePassengerSuccessfully() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.of(passenger));

        passengerService.deletePassengerById(PASSENGER_ID);

        assertTrue(passenger.isDeleted());
        verify(passengerRepository, times(1)).save(passenger);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPassenger() {
        when(passengerRepository.findByIdAndDeletedFalse(PASSENGER_ID))
                .thenReturn(Optional.empty());

        assertThrows(PassengerNotFoundException.class, () ->
                passengerService.deletePassengerById(PASSENGER_ID));
    }
}

