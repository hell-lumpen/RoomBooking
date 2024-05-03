package org.mai.roombooking.controllers;

import lombok.AllArgsConstructor;
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
import org.mai.roombooking.exceptions.base.BookingException;
import org.mai.roombooking.services.BookingService;
import org.mai.roombooking.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.mai.roombooking.services.UserService;
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
@AllArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    /**
     * Метод получения всех бронирований, хранящихся в базе данных
     *
     * @return список бронирований
     */
    @GetMapping("/all")
    public ResponseEntity<List<RoomBookingDTO>> getAll() {
        return ResponseEntity.ok(bookingService.getAll());
    }

    /**
     * Получение детализированной информации по бронированию
     *
     * @param bookingId идентификатор бронирования
     * @return детализированная информация о бронировании
     * @throws BookingNotFoundException бронирование с заданным идентификатором не найдено
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<RoomBookingDetailsDTO> getBooking(
            @PathVariable Long bookingId) throws BookingNotFoundException {

        RoomBookingDetailsDTO booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(booking);
    }


    /**
     * Получить все бронирования в заданном временном диапазоне, сгруппированные по комнате.
     *
     * @param startTime Дата-время начала выгрузки
     * @param endTime   Дата-время конца выгрузки
     * @return ResponseEntity со списком бронирований, сгруппированных по комнате
     */
    @GetMapping("")
    public ResponseEntity<List<Pair>> getBookingsInTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        return ResponseEntity.ok(bookingService.getBookingsInTimeRange(startTime, endTime));
    }

    /**
     * Получение списка бронирований по статусу
     *
     * @param status текущий статус бронирования
     * @return список бронирований
     */
    @GetMapping("/status")
    public List<RoomBookingDTO> getBookingsByStatus(@RequestParam String status) {
        return bookingService.getBookingsByStatus(Booking.Status.valueOf(status)).stream()
                .map(RoomBookingDTO::new)
                .toList();
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
     * @param startTime Дата-время начала выгрузки
     * @param endTime   Дата-время конца выгрузки
     * @return ResponseEntity со списком бронирований для конкретного пользователя
     */
    @GetMapping("/user")
    public ResponseEntity<List<RoomBookingDTO>> getBookingsByCUserInTimeRange(
            @RequestParam @NonNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @NonNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @AuthenticationPrincipal @NonNull User user) {

        List<RoomBookingDTO> bookings = bookingService.getBookingsByUserInTimeRange(user.getId(), startTime, endTime);
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

        if (!(Objects.equals(userId, user.getId())
                || user.getRole().equals(User.UserRole.ADMINISTRATOR)))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        List<RoomBookingDTO> bookings = bookingService.getBookingsByUserInTimeRange(userId, startTime, endTime);
        return ResponseEntity.ok(bookings);
    }


    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<RoomBookingDTO>> getBookingsByGroupInTimeRange(
            @PathVariable @NonNull Long groupId,
            @RequestParam @NonNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @NonNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ) {
        return ResponseEntity.ok(bookingService.getBookingsByGroupInTimeRange(groupId, startTime, endTime)
                .stream()
                .map(RoomBookingDTO::new)
                .toList());
    }

    /**
     * Получение предстоящих событий авторизованного пользователя
     *
     * @param limit количество запрашиваемых событий
     * @param user  авторизованный пользователя
     * @return список бронирований
     */
    @GetMapping("/first{limit}/")
    public List<RoomBookingDTO> getLimitBookingsByUser(
            @PathVariable @NonNull Integer limit,
            @AuthenticationPrincipal @NonNull User user) {
        return bookingService.getLastBookingsByOwner(user.getId(), limit)
                .stream()
                .map(RoomBookingDTO::new)
                .toList();
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
    public ResponseEntity<RoomBookingDetailsDTO> createBooking(
            @RequestBody @NonNull RoomBookingRequestDTO request,
            @AuthenticationPrincipal @NonNull User user)
            throws BookingException, RoomNotFoundException, UserNotFoundException {

        // Добавление создателя брони, если он не установлен в запросе
        if (request.getOwnerId() == null)
            request.setOwnerId(user.getId());

        // Попытка брони на другого человека без прав администратора
        if (!(Objects.equals(request.getOwnerId(), user.getId())
                || user.getRole().equals(User.UserRole.ADMINISTRATOR)))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        var userRequest = userService.findById(request.getOwnerId());
        if (userRequest.isPresent() && userRequest.get().getRole().equals(User.UserRole.AUTHORISED))
            request.setStatus(Booking.Status.REQUIRES_CONFIRMATION);
        else
            request.setStatus(Booking.Status.CONFIRMED);

        Booking createdBooking = bookingService.updateBooking(request);

        // TODO: не только добавление, но и изменение бронирования
        messagingTemplate.convertAndSend("/topic/1", "add new");
        return ResponseEntity.ok(new RoomBookingDetailsDTO(createdBooking));
    }

    /**
     * Метод изменения бронирования
     *
     * @param request DTO с информацией для изменения бронирования
     * @return ResponseEntity с обновленным бронированием
     * @throws AccessDeniedException попытка добавления бронирования на другого пользователя без прав администратора
     */
    @PutMapping
    public ResponseEntity<RoomBookingDetailsDTO> updateBooking(
            @RequestBody @NonNull RoomBookingRequestDTO request,
            @AuthenticationPrincipal @NonNull User user) throws BookingException {
        messagingTemplate.convertAndSend("/topic/1", "add new");
        return createBooking(request, user);
    }

    /**
     * @param bookingId Идентификатор бронирования не null
     * @return 200 при успешном удалении, другой код в случе ошибки
     * @throws BookingNotFoundException попытка удаления несуществующего бронирования
     * @throws AccessDeniedException    недостаточно прав для удаления текущего бронирования
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> deleteBooking(
            @PathVariable @NonNull Long bookingId,
            @AuthenticationPrincipal @NonNull User user)
            throws BookingNotFoundException, AccessDeniedException {

        var booking = bookingService.getBookingById(bookingId);

        if (!Objects.equals(booking.getOwner().getId(), user.getId())
                && !user.getRole().equals(User.UserRole.ADMINISTRATOR))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        bookingService.deleteBooking(bookingId);
        messagingTemplate.convertAndSend("/topic/1", "add new");
        return ResponseEntity.ok("Booking deleted successfully");
    }

}