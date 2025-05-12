package com.master.reservationservice.ReservationService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateReservationRequest {
    @NotBlank
    private String userId;

    @Email
    private String email;

    @NotBlank
    private String eventId;

    @Min(1)
    private int seats;
}
