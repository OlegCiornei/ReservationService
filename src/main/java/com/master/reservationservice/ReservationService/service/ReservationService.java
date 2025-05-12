package com.master.reservationservice.ReservationService.service;

import java.time.LocalDateTime;
import java.util.UUID;

import com.master.reservationservice.ReservationService.dto.CreateReservationRequest;
import com.master.reservationservice.ReservationService.event.ReservationRequestEvent;
import com.master.reservationservice.ReservationService.model.ReservationStatus;
import com.master.reservationservice.ReservationService.model.exception.NoAvailableSeatsException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final SeatAvailabilityService seatAvailabilityService;
    private final KafkaTemplate<String, ReservationRequestEvent> kafkaTemplate;

    public void handleReservation(CreateReservationRequest request) {
        String eventId = request.getEventId();
        int seatsRequested = request.getSeats();

        Integer available = redisTemplate.opsForValue().get(eventId);

        if (available == null) {
            available = seatAvailabilityService.fetchAvailableSeats(eventId).getAvailableSeats();
            redisTemplate.opsForValue().set(eventId, available);
        }

        if (available < seatsRequested) {
            throw new NoAvailableSeatsException("Not enough seats for event " + eventId);
        }

        String reservationId = UUID.randomUUID().toString();
        ReservationRequestEvent event = ReservationRequestEvent.builder()
                .reservationId(reservationId)
                .userId(request.getUserId())
                .eventId(eventId)
                .email(request.getEmail())
                .seats(seatsRequested).status(ReservationStatus.CREATED)
                .createdAt(LocalDateTime.now()).build();

        kafkaTemplate.send("reservation-requests", reservationId, event);
    }
}
