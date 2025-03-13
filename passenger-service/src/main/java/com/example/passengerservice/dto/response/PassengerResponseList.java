package com.example.passengerservice.dto.response;

import java.util.List;

public record PassengerResponseList(
        List<PassengerResponse> responseList
) {
}
