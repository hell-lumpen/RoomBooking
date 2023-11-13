package org.mai.roombooking.exceptions.base;

import lombok.Getter;

@Getter
public class BookingException extends Exception {
    private final Long bookingId;
    public BookingException(String message) {
        super(message);
        bookingId = null;
    }

    public BookingException(String message, Long bookingId) {
        super(message);
        this.bookingId = bookingId;
    }
}
