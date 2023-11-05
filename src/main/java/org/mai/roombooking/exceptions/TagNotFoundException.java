package org.mai.roombooking.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Tag not found")
@Getter
public class TagNotFoundException extends RuntimeException {
    private final String name;
    public TagNotFoundException(String name) {
        super("Not found. Tag with short name " + name + " not found");
        this.name = name;
    }
}
