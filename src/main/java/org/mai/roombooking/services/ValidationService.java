package org.mai.roombooking.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.NonNull;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;
import org.mai.roombooking.exceptions.BookingConflictException;
import org.mai.roombooking.exceptions.CustomValidationException;
import org.mai.roombooking.exceptions.base.BookingException;
import org.mai.roombooking.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class ValidationService {
    private final Validator validator;
    private final BookingRepository bookingRepository;
    @Autowired
    ValidationService(Validator validator, BookingRepository bookingRepository) {
        this.validator = validator;
        this.bookingRepository = bookingRepository;
    }

    public <T> void validate(T value) {
        StringBuilder errors = new StringBuilder();
        Set<ConstraintViolation<T>> violations = validator.validate(value);
        for (var violation : violations) {
            errors.append(violation.getMessage());
        }

        if (!errors.isEmpty())
            throw new CustomValidationException(errors.toString());
    }

    public void validateBooking(@NonNull LocalDateTime start,
                                 @NonNull LocalDateTime end,
                                 @NonNull Long roomId,
                                 Long bookingId) throws BookingException {
        if (start.isAfter(end))
            throw new BookingException("The start time of the booking must not be later than the end time.");
        else if (start.getDayOfYear() != end.getDayOfYear())
            throw new BookingException("Reservations must start and end on the same day.");

        var conflicts = bookingRepository.findBookingsInDateRange(start, end)
                .stream()
                .filter((bookingItem -> bookingItem.getRoom().getRoomId().equals(roomId)))
                .map(RoomBookingDTO::new)
                .toList();

        if (!conflicts.isEmpty() && !(conflicts.size() == 1 && conflicts.get(0).getId().equals(bookingId)))
            throw new BookingConflictException(conflicts);
    }
}
