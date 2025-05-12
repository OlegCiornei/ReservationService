package com.master.reservationservice.ReservationService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatInfoResponse {
    private String eventId;
    private int availableSeats;
}