package org.mai.roombooking.controllers;

import lombok.NonNull;
import org.mai.roombooking.dtos.LessonDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;
import org.mai.roombooking.exceptions.DTOValidationException;
import org.mai.roombooking.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/lesson")
    public RoomBookingDTO addLesson(@NonNull @RequestBody LessonDTO dto) {
        if (dto.getRoom().equals("--каф"))
            throw new DTOValidationException("--каф комната невалидна");

        return scheduleService.bookingLesson(dto);


    }

    @PostMapping("/schedule")
    public void addSchedule(@NonNull @RequestBody List<LessonDTO> dtos) {
        // TODO: реализация
    }
}
