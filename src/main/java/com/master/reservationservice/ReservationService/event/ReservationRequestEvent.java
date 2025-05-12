package com.master.reservationservice.ReservationService.event;

import java.time.LocalDateTime;

import com.master.reservationservice.ReservationService.model.ReservationStatus;

import org.apache.kafka.common.protocol.types.Field;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationRequestEvent {

    private String reservationId;
    private String userId;
    private String eventId;
    private String email;
    private int seats;
    private ReservationStatus status;
    private LocalDateTime createdAt;
}
