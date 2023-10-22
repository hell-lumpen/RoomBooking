package org.mai.roombooking.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Booking not found")
@Getter
public class RoomNotFoundException extends RuntimeException {
    private final Long id;
    public RoomNotFoundException(Long id) {
        super("Not found. Booking with id " + id + " not found");
        this.id = id;
    }
}
