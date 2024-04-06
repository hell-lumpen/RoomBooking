package org.mai.roombooking.controllers;

import lombok.AllArgsConstructor;
import org.mai.roombooking.dtos.RoomDTO;
import org.mai.roombooking.services.RoomService;
import org.mai.roombooking.services.ValidationService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;
    private final ValidationService validationService;

    /**
     * Получение списка всех комнат
     * @return список всех комнат
     */
    @GetMapping("/all")
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms().stream()
                .map(RoomDTO::new)
                .toList());
    }

    /**
     * Получение списка кафедральных аудиторий
     * @return список кафедральных аудиторий
     */
    @GetMapping("/get/cathedral")
    public ResponseEntity<List<RoomDTO>> getCathedralRooms() {
        return ResponseEntity.ok(roomService.getCathedralRooms().stream()
                .map(RoomDTO::new)
                .toList());
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

    /**
     * Добавление/обновление данных по аудитории
     * @param dto данные по аудитории
     * @return обновленные данные
     */
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<RoomDTO> updateRoom(@NonNull @RequestBody RoomDTO dto) {
        validationService.validate(dto);
        return ResponseEntity.ok(new RoomDTO(roomService.update(dto)));
    }

    /**
     * Удаление аудитории
     * @param id идентификатор аудитории
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<String> deleteRoom(@NonNull @PathVariable Long id) {
        try{
            roomService.delete(id);
        } catch (EmptyResultDataAccessException ex) {
            return new ResponseEntity<>("Попытка удаления несуществующей аудитории",
                    HttpStatusCode.valueOf(400));
        }
        return ResponseEntity.ok("Удаление прошло успешно");
    }
}
