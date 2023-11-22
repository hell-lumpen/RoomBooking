package org.mai.roombooking.controllers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mai.roombooking.dtos.bookings.Pair;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingDetailsDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingRequestDTO;
import org.mai.roombooking.dtos.RoomDTO;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.exceptions.BookingConflictException;
import org.mai.roombooking.exceptions.BookingNotFoundException;
import org.mai.roombooking.exceptions.RoomNotFoundException;
import org.mai.roombooking.exceptions.UserNotFoundException;
import org.mai.roombooking.services.BookingService;
import org.mai.roombooking.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public BookingController(BookingService bookingService, SimpMessagingTemplate messagingTemplate) {
        this.bookingService = bookingService;
        this.messagingTemplate = messagingTemplate;
    }


    /**
     * Метод получения детализированной информации по конкретному бронированию
     * @param id идентификатор бронирования
     * @return детализированная информация по бронированию
     * @throws BookingNotFoundException попытка получения несуществующего бронирования
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomBookingDetailsDTO> getBookingDetails(@PathVariable @NonNull Long id)
            throws BookingNotFoundException {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }


    /**
     * Метод получения всех бронирований, хранящихся в базе данных
     * @return список бронирований
     */
    @GetMapping("/all")
    public ResponseEntity<List<RoomBookingDTO>> getAll() {
        return ResponseEntity.ok(bookingService.getAll());
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
            @PathVariable @NonNull Long userId,
            @RequestParam @NonNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @NonNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @AuthenticationPrincipal @NonNull User user) {

        if(!(Objects.equals(userId, user.getUserId())
                || user.getRole().equals(User.UserRole.ADMINISTRATOR)))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        List<RoomBookingDTO> bookings = bookingService.getBookingsByUserInTimeRange(userId, startTime, endTime);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Метод изменения или создания нового бронирования аудитории
     * @param request DTO с информацией для изменения бронирования
     * @return ResponseEntity с обновленным бронированием
     */
    @PutMapping()
    public ResponseEntity<RoomBookingDetailsDTO> updateBooking(
            @RequestBody @NonNull RoomBookingRequestDTO request,
            @AuthenticationPrincipal @NonNull User user) throws BookingConflictException {

        if(request.getOwnerId() == null ||
            !(Objects.equals(request.getOwnerId(), user.getUserId())
                    || user.getRole().equals(User.UserRole.ADMINISTRATOR)))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        return ResponseEntity.ok(new RoomBookingDetailsDTO(bookingService.updateBooking(request)));
    }

    /**
     * @param bookingId Идентификатор бронирования не null
     * @return 200 при успешном удалении, другой код в случе ошибки
     * @throws BookingNotFoundException попытка удаления несуществующего бронирования
     * @throws AccessDeniedException недостаточно прав для удаления текущего бронирования
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> deleteBooking(
            @PathVariable @NonNull Long bookingId,
            @AuthenticationPrincipal @NonNull User user)
            throws BookingNotFoundException, AccessDeniedException {

        if(!Objects.equals(bookingService.getBookingById(bookingId).getOwner().getId(), user.getUserId())
                && !user.getRole().equals(User.UserRole.ADMINISTRATOR))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        bookingService.deleteBooking(bookingId);

        return ResponseEntity.ok("Booking deleted successfully");
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<RoomBookingDetailsDTO> getBooking(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal User user) throws BookingNotFoundException, InterruptedException {

        if(!Objects.equals(bookingService.getBookingById(bookingId).getOwner().getId(), user.getUserId())
                && !user.getRole().equals(User.UserRole.ADMINISTRATOR))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        RoomBookingDetailsDTO booking = bookingService.getBookingById(bookingId);
        Thread.sleep(2000);
        return ResponseEntity.ok(booking);
    }

    /**
     * Создать бронирование.
     *
     * @param request DTO с информацией о создании бронирования
     * @return ResponseEntity с созданным бронированием
     * @throws RoomNotFoundException если комната не найдена по идентификатору
     * @throws UserNotFoundException если пользователь не найден по идентификатору
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody @NonNull RoomBookingRequestDTO request,
            @AuthenticationPrincipal @NonNull User user)
            throws BookingConflictException, RoomNotFoundException, UserNotFoundException {

        if (request.getOwnerId() == null)
            request.setOwnerId(user.getUserId());

        if(!(Objects.equals(request.getOwnerId(), user.getUserId())
                || user.getRole().equals(User.UserRole.ADMINISTRATOR)))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        Booking createdBooking = bookingService.updateBooking(request);

        messagingTemplate.convertAndSend("/topic/1", "add new");
        return ResponseEntity.ok(createdBooking);
    }

//    //TODO: Перенести в Room service
//    /**
//     * Получить список доступных комнат для бронирования в заданном временном диапазоне с учетом дополнительных параметров.
//     *
//     * @param startTime     Дата-время начала бронирования
//     * @param endTime       Дата-время окончания бронирования
//     * @param capacity      Вместимость комнаты (опционально)
//     * @param hasProjector  Наличие проектора в комнате (опционально)
//     * @param hasComputers  Наличие компьютеров в комнате (опционально)
//     * @return ResponseEntity со списком доступных комнат
//     */
//    @GetMapping("/available-rooms")
//    public ResponseEntity<List<RoomDTO>> getAvailableRooms(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
//            @RequestParam(required = false) Integer capacity,
//            @RequestParam(required = false) Boolean hasProjector,
//            @RequestParam(required = false) Boolean hasComputers) {
//
//        List<RoomDTO> availableRooms = roomService.getAvailableRooms(startTime, endTime, capacity, hasProjector, hasComputers);
//        return ResponseEntity.ok(availableRooms);
//    }
}