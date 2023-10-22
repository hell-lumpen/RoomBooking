package org.mai.roombooking.controllers;

import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.dto.BookingDTO;
import org.mai.roombooking.security.requestDTO.UserLoginRequest;
import org.mai.roombooking.security.requestDTO.UserRegistrationRequest;
import org.mai.roombooking.security.requestDTO.AuthResponse;
import org.mai.roombooking.repositories.BookingRepository;
import org.mai.roombooking.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final BookingRepository bookingRepository;


    @Autowired
    public AuthController(AuthService authService, BookingRepository bookingRepository) {
        this.authService = authService;
        this.bookingRepository = bookingRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        return ResponseEntity.ok(authService.registerUser(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody UserLoginRequest loginRequest) {
        return ResponseEntity.ok(authService.loginUser(loginRequest));
    }

    @GetMapping("/hui")
    public Map<String, List<Booking>> getBookingsByTimeRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") LocalDateTime endTime) {
        Map<String, List<Booking>> booking = null;
        try {
            booking = bookingRepository.findBookingsInDateRangeAndGroupByRoomName(startTime, endTime);
        }
        catch (Exception psqlException) {
            System.err.println(psqlException.getMessage());
        }
        return booking;
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
//        authService.logoutUser(request);
//        return ResponseEntity.ok("User logged out successfully");
//    }
}
