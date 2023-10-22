package org.mai.roombooking.services;

import org.mai.roombooking.dtos.RoomBookingDTO;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.repositories.BookingRepository;
import org.mai.roombooking.repositories.RoomRepository;
import org.mai.roombooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервисный класс для управления бронированиями.
 */
@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;


    /**
     * Получает все бронирования комнат в заданном временном диапазоне.
     *
     * @param startTime дата и время начала запроса
     * @param endTime   дата и время окончания запроса
     * @return список бронирований комнат в заданном временном диапазоне
     */
    public List<RoomBookingDTO> getBookingsInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> bookings = bookingRepository.findBookingsInDateRange(startTime,endTime);
        return bookings.stream().map((RoomBookingDTO::new)).toList();
    }

    /**
     * Получает бронирования для конкретной комнаты в заданном временном диапазоне.
     *
     * @param roomId    идентификатор комнаты
     * @param startTime дата и время начала запроса
     * @param endTime   дата и время окончания запроса
     * @return список бронирований для указанной комнаты в заданном временном диапазоне
     */
    public List<RoomBookingDTO> getBookingsByRoomInTimeRange(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> bookings = bookingRepository.findBookingsInDateRangeForRoom(startTime, endTime, roomId);
        return bookings.stream().map((RoomBookingDTO::new)).toList();
    }

    /**
     * Получает бронирования для конкретного пользователя в заданном временном диапазоне.
     *
     * @param userId    идентификатор пользователя
     * @param startTime дата и время начала запроса
     * @param endTime   дата и время окончания запроса
     * @return список бронирований для указанного пользователя в заданном временном диапазоне
     * @throws UsernameNotFoundException если пользователь не найден по идентификатору
     */

    public List<RoomBookingDTO> getBookingsByUserInTimeRange(Long userId,
                                                             LocalDateTime startTime,
                                                             LocalDateTime endTime)
                                                             throws UsernameNotFoundException{
        List<Booking> bookings = bookingRepository.findBookingsInDateRangeByUser(startTime, endTime, userId);

        if (bookings.isEmpty())
            userRepository.findById(userId).orElseThrow(() ->
                    new UsernameNotFoundException("User whis id" + userId +  "not found"));

        return bookings.stream().map((RoomBookingDTO::new)).toList();
    }

    /**
     * Обновляет периодическое бронирование на основе предоставленного запроса.
     *
     * @param bookingId идентификатор бронирования для обновления
     * @param request   запрос с информацией для обновления бронирования
     * @return обновленное бронирование
     * @throws BookingNotFoundException если бронирование не найдено по идентификатору
     */
    public BookingDTO updatePeriodicBooking(Long bookingId, BookingUpdateRequestDTO request) {
        return null;
    }

    /**
     * Обновляет отдельное бронирование на основе предоставленного запроса.
     *
     * @param bookingId идентификатор бронирования для обновления
     * @param request   запрос с информацией для обновления бронирования
     * @return обновленное бронирование
     * @throws BookingNotFoundException если бронирование не найдено по идентификатору
     */
    public BookingDTO updateBooking(Long bookingId, BookingUpdateRequestDTO request) {
        return null;
    }

    /**
     * Удаляет периодическое бронирование на основе предоставленного идентификатора.
     *
     * @param bookingId идентификатор бронирования для удаления
     * @throws BookingNotFoundException если бронирование не найдено по идентификатору
     */
    public void deletePeriodicBooking(Long bookingId) {
        // Реализация деталей
    }

    /**
     * Удаляет отдельное бронирование на основе предоставленного идентификатора.
     *
     * @param bookingId идентификатор бронирования для удаления
     * @throws BookingNotFoundException если бронирование не найдено по идентификатору
     */
    public void deleteBooking(Long bookingId) {
        // Реализация деталей
    }

    /**
     * Создает новое бронирование на основе предоставленного запроса.
     *
     * @param request запрос с информацией для создания бронирования
     * @return созданное бронирование
     * @throws RoomNotFoundException   если комната не найдена по идентификатору
     * @throws UserNotFoundException   если пользователь не найден по идентификатору
     * @throws BookingConflictException если есть конфликт с существующим бронированием
     */
    public BookingDTO createBooking(BookingCreateRequestDTO request) {
        return null;
    }
}