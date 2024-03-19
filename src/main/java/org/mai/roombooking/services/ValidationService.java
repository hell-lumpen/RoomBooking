package org.mai.roombooking.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.NonNull;
import org.mai.roombooking.dtos.PairDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.RecurringException;
import org.mai.roombooking.entities.RecurringRule;
import org.mai.roombooking.exceptions.BookingConflictException;
import org.mai.roombooking.exceptions.CustomValidationException;
import org.mai.roombooking.exceptions.base.BookingException;
import org.mai.roombooking.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public List<Booking> checkBookingRecurring(List<Booking> bookings,
                                               LocalDateTime start,
                                               LocalDateTime end) {
        return (checkBookingRecurring(bookings, start, end, false));
    }

    public List<Booking> checkBookingRecurring(List<Booking> bookings,
                                               LocalDateTime start,
                                               LocalDateTime end,
                                               boolean changeTime) {

        List<Booking> resulBookings = new ArrayList<>(bookings.stream()
                .filter(booking -> booking.getRecurringRule() == null)
                .toList());

        bookings.stream()
                .filter(booking -> booking.getRecurringRule() != null)
                .toList()
                .forEach(
                        booking -> {
                            PairDTO key = new PairDTO(booking.getRoom());
                            RecurringRule recurringRule = booking.getRecurringRule();
                            LocalDateTime s, e;
                            boolean compareWithException;
                            long startCountRecurringDays = 0;
                            if (recurringRule.getUnit().equals("DAY"))
                                startCountRecurringDays = 1;
                            else if (recurringRule.getUnit().equals("WEEK")) {
                                startCountRecurringDays = 7;
                            }

                            startCountRecurringDays *= recurringRule.getInterval();

                            for (int i = 0; i < recurringRule.getCount(); i++) {
                                compareWithException = false;
                                s = booking.getStartTime().plusDays(i * startCountRecurringDays);
                                e = booking.getEndTime().plusDays(i * startCountRecurringDays);
                                for (RecurringException exception : booking.getRecurringRule().getExceptions()) {
                                    compareWithException |= (exception.getDate().isEqual(s.toLocalDate()));
                                }


                                if (s.isBefore(end) && e.isAfter(start) && !compareWithException) {
                                    resulBookings.add(
                                            Booking.builder()
                                                    .id(booking.getId())
                                                    .bookingGroupId(booking.getBookingGroupId())
                                                    .room(booking.getRoom())
                                                    .owner(booking.getOwner())
                                                    .staff(booking.getStaff())
                                                    .groups(booking.getGroups())
                                                    .startTime(changeTime ? s : booking.getStartTime())
                                                    .endTime(changeTime ? e : booking.getEndTime())
                                                    .title(booking.getTitle())
                                                    .description(booking.getDescription())
                                                    .tags(booking.getTags())
                                                    .recurringRule(booking.getRecurringRule())
                                                    .build()
                                    );
                                }
                            }

                        }

                );

        return resulBookings;

    }

    public void validateBooking(@NonNull LocalDateTime start,
                                @NonNull LocalDateTime end,
                                @NonNull Long roomId,
                                Long bookingId) throws BookingException {
        if (start.isAfter(end))
            throw new BookingException("The start time of the booking must not be later than the end time.");
        else if (start.getDayOfYear() != end.getDayOfYear())
            throw new BookingException("Reservations must start and end on the same day.");

        var conflicts =
                checkBookingRecurring(bookingRepository.findBookingsInDateRange(start, end),
                        start, end, true)
                        .stream()
                        .filter((bookingItem -> (bookingItem.getRoom().getRoomId().equals(roomId)) && !bookingItem.getEndTime().isEqual(start)))
                        .map(RoomBookingDTO::new)
                        .toList();

        if (!conflicts.isEmpty() && !(conflicts.size() == 1 && conflicts.get(0).getId().equals(bookingId)))
            throw new BookingConflictException(conflicts);
    }
}
