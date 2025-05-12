package com.master.reservationservice.ReservationService.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import com.master.reservationservice.ReservationService.dto.SeatInfoResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatAvailabilityServiceImpl implements SeatAvailabilityService {

    private final RestTemplate restTemplate;

    @Override
    @Retry(name = "availabilityRetry", fallbackMethod = "fallback")
    @CircuitBreaker(name = "availabilityCB", fallbackMethod = "fallback")
    public SeatInfoResponse fetchAvailableSeats(String eventId) {
        String url = "http://localhost:8081/api/seats/" + eventId;
        ResponseEntity<SeatInfoResponse> response = restTemplate.getForEntity(url, SeatInfoResponse.class);
        SeatInfoResponse body = response.getBody();
        log.info("Obtained " + eventId + " = " + body);
        return body;
    }

    public Integer fallback(String eventId, Throwable ex) {
        return 0;
    }

}
