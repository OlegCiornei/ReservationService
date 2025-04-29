package com.master.reservationservice.ReservationService.event;

import java.time.LocalDateTime;

import com.master.reservationservice.ReservationService.model.ReservationStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationRequestEvent {

    private String reservationId;
    private String userId;
    private String eventId;
    private int seatsRequested;
    private ReservationStatus status;
    private LocalDateTime createdAt;
}
