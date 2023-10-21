package org.mai.roombooking.controllers;

import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.RecurrentUnit;
import org.mai.roombooking.entities.Room;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.entities.dto.BookingDTO;
import org.mai.roombooking.entities.dto.BookingDetailsDTO;
import org.mai.roombooking.repositories.BookingRepository;
import org.mai.roombooking.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/rooms/get")
    public Map<String, List<BookingDTO>> getBookingsByTimeRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") LocalDateTime endTime) {
        return bookingService.getBookingsByTimeRange(startTime, endTime);
    }

    @GetMapping("/rooms/{id}/get")
    public List<BookingDTO> getBookingsByRoom(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") LocalDateTime endTime) {
        return bookingService.getBookingsByRoomAndTimeRange(id, startTime, endTime);
    }

    @GetMapping("/{id}/details")
    public BookingDetailsDTO getBookingDetailsByRoomId(
            @PathVariable Long id
    ) {
        return bookingService.getBookingDetailsById(id);
    }

    @GetMapping("/test")
    public String doSecureTest() {
        return "secure test";
    }
}

