package com.master.reservationservice.ReservationService.service;

public interface SeatAvailabilityService {
    Integer fetchAvailableSeats(String eventId);
}
