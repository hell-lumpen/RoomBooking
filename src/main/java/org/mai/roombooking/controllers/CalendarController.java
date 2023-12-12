package org.mai.roombooking.controllers;

import lombok.NonNull;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.services.CalendarService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * Получение списка всех бронирований (доступно авторизованным пользователям с ролью "администратор")
     * @return строка формат ICal
     */
    @GetMapping("/all")
    public ResponseEntity<byte[]> getAll(@AuthenticationPrincipal @NonNull User user) {
        if (user.getRole() != User.UserRole.ADMINISTRATOR)
            throw new AccessDeniedException("Access denied: Not enough permissions");

        return configureResponse(calendarService.getAllBookings());
    }

//    @GetMapping("/group/{id}")
//    public ResponseEntity<byte[]> getByGroup(@PathVariable @NonNull Long id) {
//        return configureResponse(calendarService.getByGroup(id));
//    }
//
//    @GetMapping("/group/name/{name}")
//    public ResponseEntity<byte[]> getByGroup(@PathVariable @NonNull String name) {
//        return  configureResponse(calendarService.getByGroup(name));
//    }

//    @GetMapping("/staff/{id}")
//    public ResponseEntity<byte[]> getByStaff(@PathVariable @NonNull Long id,
//                                             @AuthenticationPrincipal @NonNull User user) {
//        if(!(Objects.equals(id, user.getUserId())
//                || user.getRole().equals(User.UserRole.ADMINISTRATOR)))
//            throw new AccessDeniedException("Access denied: Not enough permissions");
//
//        return configureResponse(calendarService.getByStaff(id));
//    }

    private @NonNull ResponseEntity<byte[]> configureResponse(@NonNull String data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("filename", "calendar.ics");

        byte[] contentBytes = data.getBytes();

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(contentBytes.length)
                .body(contentBytes);
    }
}
