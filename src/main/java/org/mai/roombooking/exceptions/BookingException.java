package org.mai.roombooking.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Booking not found")
@Getter
public class BookingException extends RuntimeException {
    public BookingException() {
        super("booking a room in a given time interval is not available");
    }
}
