package org.mai.roombooking.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.mai.roombooking.dtos.bookings.Pair;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingRequestDTO;
import org.mai.roombooking.dtos.notifications.BookingNotificationDTO;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.exceptions.BookingNotFoundException;
import org.mai.roombooking.exceptions.RoomNotFoundException;
import org.mai.roombooking.exceptions.UserNotFoundException;
import org.mai.roombooking.exceptions.base.BookingException;
import org.mai.roombooking.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST-контроллер для управления бронированиями.
 */
@Slf4j
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
//    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BookingService bookingService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;
//    private final Producer<String, String> kafkaPproducer;

    @Value("${kafka.notification.topic}")
    private String notificationTopic;

    @Autowired
    public BookingController(BookingService bookingService, SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper
//                             Producer<String, String> kafkaPproducer
    ) {
        this.bookingService = bookingService;
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
//        this.kafkaPproducer = kafkaPproducer;
    }

    /**
     * Метод получения всех бронирований, хранящихся в базе данных
     * @return список бронирований
     */ 
    @GetMapping("/all")
    public ResponseEntity<List<RoomBookingDTO>> getAll() {
        return ResponseEntity.ok(bookingService.getAll().stream().map(RoomBookingDTO::new).toList());
    }

    /**
     * Получение детализированной информации по бронированию
     * @param bookingId идентификатор бронирования
     * @return детализированная информация о бронировании
     * @throws BookingNotFoundException бронирование с заданным идентификатором не найдено
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<RoomBookingDTO> getBooking(
            @PathVariable Long bookingId) throws BookingNotFoundException {

        Booking booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(new RoomBookingDTO(booking));
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
    public ResponseEntity<List<RoomBookingDTO>> getBookingsByUserInTimeRange(
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

        if(!(Objects.equals(userId, user.getId())
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
     * @param limit количество запрашиваемых событий
     * @param user авторизованный пользователя
     * @return список бронирований
     */
    @GetMapping("/first{limit}")
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
     * @param request DTO с информацией о создании бронирования
     * @return ResponseEntity с созданным бронированием
     * @throws RoomNotFoundException если комната не найдена по идентификатору
     * @throws UserNotFoundException если пользователь не найден по идентификатору
     */
    @PostMapping
    public ResponseEntity<RoomBookingDTO> createBooking(
            @RequestBody @NonNull RoomBookingRequestDTO request,
            @AuthenticationPrincipal @NonNull User user)
            throws BookingException, RoomNotFoundException, UserNotFoundException {

        if (request.getOwnerId() == null)
            request.setOwnerId(user.getId());

        if(!(Objects.equals(request.getOwnerId(), user.getId())
                || user.getRole().equals(User.UserRole.ADMINISTRATOR)))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        if (user.getRole().equals(User.UserRole.AUTHORISED))
            request.setStatus(Booking.Status.REQUIRES_CONFIRMATION);
        else
            request.setStatus(Booking.Status.CONFIRMED);

        Booking createdBooking = bookingService.updateBooking(request);

        messagingTemplate.convertAndSend("/topic/1", "add new");
        send_notification(createdBooking);
        return ResponseEntity.ok(new RoomBookingDTO(createdBooking));
    }

    /**
     * Метод изменения бронирования
     * @param request DTO с информацией для изменения бронирования
     * @return ResponseEntity с обновленным бронированием
     * @throws AccessDeniedException попытка добавления бронирования на другого пользователя без прав администратора
     */
    @PutMapping
    public ResponseEntity<RoomBookingDTO> updateBooking(
            @RequestBody @NonNull RoomBookingRequestDTO request,
            @AuthenticationPrincipal @NonNull User user) throws BookingException {

        if (request.getOwnerId() == null)
            request.setOwnerId(user.getId());

        if(!(Objects.equals(request.getOwnerId(), user.getId())
                || user.getRole().equals(User.UserRole.ADMINISTRATOR)))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        var savedBooking = bookingService.getBookingById(request.getId());
        if (!savedBooking.getStatus().equals(request.getStatus()) &&
                !user.getRole().equals(User.UserRole.ADMINISTRATOR))
            throw new AccessDeniedException("Попытка изменения статуса бронирования без роли администратора");

        Booking createdBooking = bookingService.updateBooking(request);
        messagingTemplate.convertAndSend("/topic/1", "add new");
        send_notification(savedBooking);
        return ResponseEntity.ok(new RoomBookingDTO(createdBooking));
    }

    @PutMapping("/status")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<String> updateStatus(@RequestParam @NonNull Long bookingId,
                                               @RequestParam @NonNull Booking.Status status) {
        
        Optional<Booking> booking = bookingService.setStatus(status, bookingId);
        if (booking.isEmpty()) { return new ResponseEntity<>("Boking id not valid", HttpStatusCode.valueOf(400)); }
        send_notification(booking.get());
        return ResponseEntity.ok("Обновление статуса прошло успешно");
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

        var booking  = bookingService.getBookingById(bookingId);

        if(!Objects.equals(booking.getOwner().getId(), user.getId())
                && !user.getRole().equals(User.UserRole.ADMINISTRATOR))
            throw new AccessDeniedException("Access denied: Not enough permissions");

        bookingService.deleteBooking(bookingId);
        messagingTemplate.convertAndSend("/topic/1", "add new");
        send_notification(booking);
        return ResponseEntity.ok("Booking deleted successfully");
    }

    private void send_notification(Booking booking) {
//        try {
//            var bookingNotification = new BookingNotificationDTO(BookingNotificationDTO.Action.DELETE,
//                    new RoomBookingDTO(booking));
//            kafkaPproducer.send(new ProducerRecord<>(notificationTopic, objectMapper.writeValueAsString(bookingNotification)));
//        } catch (JsonProcessingException e) {
//            log.error("Error on parsing booking object. " + e.getMessage());
//        }
    }

}