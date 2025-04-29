package com.master.reservationservice.ReservationService.service;

import java.time.LocalDateTime;
import java.util.UUID;

import com.github.benmanes.caffeine.cache.Cache;
import com.master.reservationservice.ReservationService.dto.CreateReservationRequest;
import com.master.reservationservice.ReservationService.event.ReservationRequestEvent;
import com.master.reservationservice.ReservationService.model.ReservationStatus;
import com.master.reservationservice.ReservationService.model.exception.NoAvailableSeatsException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final Cache<String, Integer> localCache; // Caffeine
    private final RedisTemplate<String, Integer> redisTemplate;
    private final SeatAvailabilityService seatAvailabilityService;
    private final KafkaTemplate<String, ReservationRequestEvent> kafkaTemplate;

    public ReservationService(Cache<String, Integer> localCache,
                              RedisTemplate<String, Integer> redisTemplate,
                              SeatAvailabilityService seatAvailabilityService,
                              KafkaTemplate<String, ReservationRequestEvent> kafkaTemplate) {
        this.localCache = localCache;
        this.redisTemplate = redisTemplate;
        this.seatAvailabilityService = seatAvailabilityService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void handleReservation(CreateReservationRequest request) {
        String eventId = request.getEventId();
        int seatsRequested = request.getSeatsRequested();

        Integer available = localCache.getIfPresent(eventId);
        if (available == null) {
            available = redisTemplate.opsForValue().get(eventId);

            if (available == null) {
                available = seatAvailabilityService.fetchAvailableSeats(eventId);

                redisTemplate.opsForValue().set(eventId, available);
                localCache.put(eventId, available);
            } else {
                localCache.put(eventId, available);
            }
        }

        if (available < seatsRequested) {
            throw new NoAvailableSeatsException("Not enough seats for event " + eventId);
        }

        String reservationId = UUID.randomUUID().toString();
        ReservationRequestEvent event = ReservationRequestEvent.builder()
                .reservationId(reservationId)
                .userId(request.getUserId())
                .eventId(eventId)
                .seatsRequested(seatsRequested).status(ReservationStatus.CREATED)
                .createdAt(LocalDateTime.now()).build();

        kafkaTemplate.send("reservation-requests", reservationId, event);
    }
}
