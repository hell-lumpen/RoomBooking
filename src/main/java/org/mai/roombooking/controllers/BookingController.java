package org.mai.roombooking.controllers;

import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.RecurrentUnit;
import org.mai.roombooking.entities.Room;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.entities.dto.BookingDTO;
import org.mai.roombooking.repositories.BookingRepository;
import org.mai.roombooking.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking/rooms")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/get")
    public List<BookingDTO> getBookingsByTimeRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") LocalDateTime endTime) {
        return bookingService.getBookingsByTimeRange(startTime, endTime);
    }

    @GetMapping("/{id}/get")
    public List<BookingDTO> getBookingsByRoom(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") LocalDateTime endTime) {
        return bookingService.getBookingsByRoomAndTimeRange(id, startTime, endTime);
    }

    @GetMapping("/save")
    public String save() {
        Optional<Booking> bookingq = bookingRepository.findById(13L);
        Booking booking = new Booking(100L,
                new Room(3L, "usernae-test", 15, true),
                new User(1L, "usernae-test", "usernae-test", "usernae-test", "usernae-test", "usernae-test"),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                "PURPOSE test",
                1,
                RecurrentUnit.WEEKLY, LocalDateTime.now().plusHours(23), LocalDateTime.now());
        bookingService.saveBooking(booking);
        return "saved";
    }
}

