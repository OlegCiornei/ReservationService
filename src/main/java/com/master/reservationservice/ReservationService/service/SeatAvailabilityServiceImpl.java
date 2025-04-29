package com.master.reservationservice.ReservationService.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatAvailabilityServiceImpl implements SeatAvailabilityService {

    private final RestTemplate restTemplate;

    @Override
    @Retry(name = "availabilityRetry", fallbackMethod = "fallback")
    @CircuitBreaker(name = "availabilityCB", fallbackMethod = "fallback")
    public Integer fetchAvailableSeats(String eventId) {
        String url = "http://seat-availability-service/api/seats/" + eventId;
        ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
        return response.getBody();
    }

    public Integer fallback(String eventId, Throwable ex) {
        return 0;
    }

}
