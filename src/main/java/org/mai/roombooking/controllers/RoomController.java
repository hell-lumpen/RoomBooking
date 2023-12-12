package org.mai.roombooking.controllers;

import org.mai.roombooking.dtos.RoomDTO;
import org.mai.roombooking.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/all")
    public List<RoomDTO> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/get/cathedral")
    public List<RoomDTO> getCathedralRooms() {
        return roomService.getCathedralRooms();
    }

    @PostMapping("/update")
    public RoomDTO updateRoom(@NonNull @RequestBody RoomDTO dto) {
        return new RoomDTO(roomService.update(dto));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRoom(@NonNull @PathVariable Long id) {
        roomService.delete(id);
    }

    /**
     * Получить список доступных комнат для бронирования в заданном временном диапазоне
     * с учетом дополнительных параметров.
     * @param startTime     Дата-время начала бронирования
     * @param endTime       Дата-время окончания бронирования
     * @param capacity      Вместимость комнаты (опционально)
     * @param hasProjector  Наличие проектора в комнате (опционально)
     * @param hasComputers  Наличие компьютеров в комнате (опционально)
     * @return ResponseEntity со списком доступных комнат
     */
    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) Boolean hasProjector,
            @RequestParam(required = false) Boolean hasComputers) {

        List<RoomDTO> availableRooms =
                roomService.getAvailableRooms(startTime, endTime, capacity, hasProjector, hasComputers).stream()
                        .map(RoomDTO::new)
                        .toList();
        return ResponseEntity.ok(availableRooms);
    }
}
