package org.mai.roombooking.exceptions;

import lombok.Getter;
import org.mai.roombooking.exceptions.base.BookingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Already been booked previously")
@Getter
public class BookingConflictException extends BookingException {
    public BookingConflictException() {
        super("Booking conflict: Selected time slot has already been booked previously");
    }
}
