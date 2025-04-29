package com.master.reservationservice.ReservationService.model.exception;

public class NoAvailableSeatsException extends RuntimeException {
    public NoAvailableSeatsException(String message) {
        super(message);
    }
}
