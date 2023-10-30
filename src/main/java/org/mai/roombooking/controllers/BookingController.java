package org.mai.roombooking.controllers;

import lombok.extern.slf4j.Slf4j;
import org.mai.roombooking.dtos.Pair;
import org.mai.roombooking.dtos.RoomBookingDTO;
import org.mai.roombooking.dtos.RoomBookingRequestDTO;
import org.mai.roombooking.dtos.RoomDTO;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.exceptions.RoomNotFoundException;
import org.mai.roombooking.exceptions.UserNotFoundException;
import org.mai.roombooking.services.BookingService;
import org.mai.roombooking.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * REST-контроллер для управления бронированиями.
 */
@Slf4j
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final RoomService roomService;
//    private final Service roomService;

    @Autowired
    public BookingController(BookingService bookingService, RoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    /**
     * Получить все бронирования в заданном временном диапазоне, сгруппированные по комнате.
     *
     * @param startTime Дата-время начала выгрузки
     * @param endTime   Дата-время конца выгрузки
     * @return ResponseEntity со списком бронирований, сгруппированных по комнате
     */
    @GetMapping
    public ResponseEntity<List<Pair>> getBookingsInTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        log.info("Get booking in range");
        return ResponseEntity.ok(bookingService.getBookingsInTimeRange(startTime, endTime));
    }

    /**
     * Получить бронирования по конкретной комнате в заданном временном диапазоне.
     *
     * @param roomId    Идентификатор комнаты
     * @param startTime Дата-время начала выгрузки
     * @param endTime   Дата-время конца выгрузки
     * @return ResponseEntity со списком бронирований для конкретной комнаты
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<RoomBookingDTO>> getBookingsByRoomInTimeRange(
            @PathVariable Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<RoomBookingDTO> bookings = bookingService.getBookingsByRoomInTimeRange(roomId, startTime, endTime);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Получить бронирования по конкретному пользователю в заданном временном диапазоне.
     *
     * @param userId    Идентификатор пользователя
     * @param startTime Дата-время начала выгрузки
     * @param endTime   Дата-время конца выгрузки
     * @return ResponseEntity со списком бронирований для конкретного пользователя
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoomBookingDTO>> getBookingsByUserInTimeRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @AuthenticationPrincipal User user) {

        if(!(Objects.equals(userId, user.getUserId())
                || user.getRole().equals(User.UserRole.ADMINISTRATOR)))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        List<RoomBookingDTO> bookings = bookingService.getBookingsByUserInTimeRange(userId, startTime, endTime);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Изменить бронирование.
     *
     * @param request   DTO с информацией для изменения бронирования
     * @return ResponseEntity с обновленным бронированием
     */
    @PutMapping("")
    public ResponseEntity<Booking> updateBooking(
            @RequestBody RoomBookingRequestDTO request,
            @AuthenticationPrincipal User user) {

        if(!user.getRole().equals(User.UserRole.ADMINISTRATOR) && request.getUserId() == null)
            throw new UserNotFoundException((long) -1);

        if (request.isPeriodic()) {
            // Обновить всю цепочку бронирования
            return ResponseEntity.ok(bookingService.updatePeriodicBooking(request, user));
        } else {
            // Обновить одно бронирование
            return ResponseEntity.ok(bookingService.updateBooking(request, user));
        }
    }

    /**
     * Удалить бронирование.
     *
     * @param bookingId Идентификатор бронирования
     * @param isPeriodic Флаг, указывающий, нужно ли удалить всю цепочку бронирования
     * @return ResponseEntity с информацией об удалении
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> deleteBooking(
            @PathVariable Long bookingId,
            @RequestParam(defaultValue = "false") boolean isPeriodic,
            @AuthenticationPrincipal User user) {

        if(!Objects.equals(bookingService.getBookingById(bookingId).getUser().getUserId(), user.getUserId())
                && !user.getRole().equals(User.UserRole.ADMINISTRATOR))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        if (isPeriodic) {
            // Удалить всю цепочку бронирования
            bookingService.deletePeriodicBooking(bookingId);
        } else {
            // Удалить одно бронирование
            bookingService.deleteBooking(bookingId);
        }

        return ResponseEntity.ok("Booking deleted successfully");
    }

    /**
     * Создать бронирование.
     *
     * @param request DTO с информацией для создания бронирования
     * @return ResponseEntity с созданным бронированием
     * @throws RoomNotFoundException   если комната не найдена по идентификатору
     * @throws UserNotFoundException   если пользователь не найден по идентификатору
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody RoomBookingRequestDTO request,
                                                 @AuthenticationPrincipal User user) {
        if (request.getUserId() == null)
            request.setUserId(user.getUserId());

        if(!(Objects.equals(request.getUserId(), user.getUserId())
                || user.getRole().equals(User.UserRole.ADMINISTRATOR)))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        Booking createdBooking;
        if (request.isPeriodic()) {
            createdBooking = bookingService.createBooking(request, user);
        } else {
            createdBooking = bookingService.updateBooking(request, user);
        }
        return ResponseEntity.ok(createdBooking);
    }

    /**
     * Получить список доступных комнат для бронирования в заданном временном диапазоне с учетом дополнительных параметров.
     *
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

        List<RoomDTO> availableRooms = roomService.getAvailableRooms(startTime, endTime, capacity, hasProjector, hasComputers);
        return ResponseEntity.ok(availableRooms);
    }

//    /**
//     * Получить список всех аудиторий со статусами на текущий момент, отсортированных по релевантности запроса.
//     * @param capacity      Вместимость комнаты (опционально)
//     * @param hasProjector  Наличие проектора в комнате (опционально)
//     * @param hasComputers  Наличие компьютеров в комнате (опционально)
//     * @return ResponseEntity со списком аудиторий и их статусами
//     */
//    @GetMapping("/status")
//    public ResponseEntity<List<RoomStatusDTO>> getAllRoomsStatus(
//            @RequestParam(required = false) Integer capacity,
//            @RequestParam(required = false) Boolean hasProjector,
//            @RequestParam(required = false) Boolean hasComputers
//    ) {
//        List<RoomStatusDTO> roomsStatus = roomService.getAllRoomsStatus(capacity, hasProjector, hasComputers);
//        return ResponseEntity.ok(roomsStatus);
//    }

}