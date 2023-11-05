package org.mai.roombooking.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Group not found")
@Getter
public class GroupNotFoundException extends RuntimeException {
    private final Long id;
    public GroupNotFoundException(Long id) {
        super("Not found. Group with id " + id + " not found");
        this.id = id;
    }
}
