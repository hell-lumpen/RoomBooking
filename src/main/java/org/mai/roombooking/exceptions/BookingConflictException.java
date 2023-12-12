package org.mai.roombooking.exceptions;

import lombok.Getter;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;
import org.mai.roombooking.exceptions.base.BookingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;


@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Already been booked previously")
@Getter
public class BookingConflictException extends BookingException {
    //TODO: пробросить список на сторону фронта
    List<RoomBookingDTO> conflicts;
    public BookingConflictException(List<RoomBookingDTO> _conflicts) {
        super("Booking conflict: Selected time slot has already been booked previously");
        conflicts = _conflicts;
    }
}
