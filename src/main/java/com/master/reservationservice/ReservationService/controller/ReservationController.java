package com.master.reservationservice.ReservationService.controller;

import jakarta.validation.Valid;

import com.master.reservationservice.ReservationService.dto.CreateReservationRequest;
import com.master.reservationservice.ReservationService.service.ReservationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestBody @Valid CreateReservationRequest request) {
        reservationService.handleReservation(request);
        return ResponseEntity.accepted().build();
    }
}
