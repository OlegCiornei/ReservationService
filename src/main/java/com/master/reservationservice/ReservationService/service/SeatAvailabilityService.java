package com.master.reservationservice.ReservationService.service;

import com.master.reservationservice.ReservationService.dto.SeatInfoResponse;

public interface SeatAvailabilityService {
    SeatInfoResponse fetchAvailableSeats(String eventId);
}
