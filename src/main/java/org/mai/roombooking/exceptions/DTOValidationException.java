package org.mai.roombooking.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Validation error")
@Getter
public class DTOValidationException extends RuntimeException {
    String error;
    public DTOValidationException(String error) {
        super("Validation error: " + error);
        this.error = error;
    }
}
