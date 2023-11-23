package org.mai.roombooking.controllers;

import org.mai.roombooking.services.CalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAll() {
        System.out.println("1");
        return ResponseEntity.ok(calendarService.getAllBookings());
    }
}
