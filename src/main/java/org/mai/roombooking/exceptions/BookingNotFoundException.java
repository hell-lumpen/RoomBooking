package org.mai.roombooking.exceptions;

import lombok.Getter;
import org.mai.roombooking.exceptions.base.BookingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Booking not found")
@Getter
public class BookingNotFoundException extends BookingException {
    private final Long id;
    public BookingNotFoundException(Long id) {
        super("Not found. Booking with id " + id + " not found");
        this.id = id;
    }
}
