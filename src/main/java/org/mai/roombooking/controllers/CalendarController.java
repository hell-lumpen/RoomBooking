package org.mai.roombooking.controllers;

import org.mai.roombooking.services.CalendarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/all")
    public String getAll() {
        return calendarService.getAllBookings();
    }
}
