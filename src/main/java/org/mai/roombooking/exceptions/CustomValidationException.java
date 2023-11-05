package org.mai.roombooking.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT, reason = "Booking not found")
@Getter
public class CustomValidationException extends RuntimeException {
    private final String error;
    public CustomValidationException(String error) {
        super("validation exception: " + error);
        this.error = error;
    }
}

