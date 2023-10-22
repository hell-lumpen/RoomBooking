package org.mai.roombooking.services;

import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.RecurrentUnit;
import org.mai.roombooking.entities.dto.BookingDTO;
import org.mai.roombooking.entities.dto.BookingDetailsDTO;
import org.mai.roombooking.exceptions.BookingNotFoundException;
import org.mai.roombooking.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookingService {

    public List<RoomBookingDTO> getBookingsInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }

    public List<BookingDTO> getBookingsByRoomInTimeRange(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }

    public List<BookingDTO> getBookingsByUserInTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }

    public BookingDTO updatePeriodicBooking(Long bookingId, BookingUpdateRequestDTO request) {
        return null;
    }

    public BookingDTO updateBooking(Long bookingId, BookingUpdateRequestDTO request) {
        return null;
    }

    public void deletePeriodicBooking(Long bookingId) {

    }

    public void deleteBooking(Long bookingId) {

    }

    public BookingDTO createBooking(BookingCreateRequestDTO request) {
        return null;
    }
}
