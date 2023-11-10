package org.mai.roombooking.services;

import lombok.NonNull;
import org.mai.roombooking.dtos.bookings.Pair;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingRequestDTO;
import org.mai.roombooking.entities.*;
import org.mai.roombooking.exceptions.*;
import org.mai.roombooking.repositories.BookingRepository;
import org.mai.roombooking.repositories.GroupRepository;
import org.mai.roombooking.repositories.RoomRepository;
import org.mai.roombooking.repositories.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервисный класс для управления бронированиями.
 */
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final GroupRepository groupRepository;

    BookingService(BookingRepository bookingRepository, UserRepository userRepository,
                   RoomRepository roomRepository, GroupRepository groupRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.groupRepository = groupRepository;
    }

    // GETERS
    public Booking getBookingById(Long bookingId) throws BookingNotFoundException {
        return bookingRepository.findById(bookingId).orElseThrow(()->new BookingNotFoundException(bookingId));
    }


    /**
     * Получает все бронирования комнат в заданном временном диапазоне.
     *
     * @param startTime дата и время начала запроса
     * @param endTime   дата и время окончания запроса
     * @return список бронирований комнат в заданном временном диапазоне
     */
    public List<Pair> getBookingsInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> bookings = bookingRepository.findBookingsInDateRange(startTime,endTime);

        Map<String, List<RoomBookingDTO>> groupedBookings = bookings.stream().map((RoomBookingDTO::new)).collect(Collectors.groupingBy(RoomBookingDTO::getRoom, Collectors.toList()));
        var data = groupedBookings.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .sorted(Comparator.comparing(RoomBookingDTO::getStartTime))
                                .toList()
                ));

        List<Pair> res = new ArrayList<>();
        for (var entry : data.entrySet()) {
            res.add(new Pair(entry));
        }
        return res;
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
     * @throws AccessDeniedException если пользователю недостаточно прав для выполнения запроса
     */
    public List<RoomBookingDTO> getBookingsByUserInTimeRange(Long userId,
                                                             LocalDateTime startTime,
                                                             LocalDateTime endTime)
                                                             throws UsernameNotFoundException{
        List<Booking> bookings = bookingRepository.findBookingsInDateRangeByUser(startTime, endTime, userId);
            if (bookings.isEmpty())
                userRepository.findById(userId).orElseThrow(() ->
                        new UsernameNotFoundException("User with id" + userId + "not found"));

        return bookings.stream().map((RoomBookingDTO::new)).toList();
    }

//    /**
//     * Обновляет периодическое бронирование на основе предоставленного запроса.
//     *
//     * @param request   запрос с информацией для обновления бронирования
//     * @return обновленное бронирование
//     */
//    @Transactional
//    public Booking updatePeriodicBooking(@NonNull RoomBookingRequestDTO request) {
//        this.deletePeriodicBooking(request.getId());
//        return this.createPrerodicBooking(request);
//    }
//
//    /**
//     * Удаляет периодическое бронирование на основе предоставленного идентификатора.
//     *
//     * @param bookingId идентификатор бронирования для удаления
//     * @throws BookingNotFoundException если бронирование не найдено по идентификатору
//     */
//    public void deletePeriodicBooking(Long bookingId) {
//        var booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new BookingNotFoundException(bookingId));
//        bookingRepository.deleteAllByPeriodicBookingId(booking.getBookingGroupId());
//    }
//
//
//    /**
//     * Создает новое бронирование на основе предоставленного запроса.
//     *
//     * @param request запрос с информацией для создания бронирования
//     * @return созданное бронирование
//     */
//    @Transactional
//    public Booking createPrerodicBooking(@NonNull RoomBookingRequestDTO request) {
//        LocalDateTime end = request.getEndTime();
//        LocalDateTime start = request.getEndTime();
//
//        var booking = getBookingFromDTO(request);
//        booking.setBookingGroupId(UUID.randomUUID());
//
//        while (end.isBefore(request.getRRule().getUntilDate())) {
//            if (validateBooking(start,end, request.getRoomId()))
//                bookingRepository.save(booking);
//            else
//                throw new BookingException();
//
//            end = RECURRING_RULES.get(request.getRRule().getFrequency())
//                    .performOperation(end, request.getRRule().getInterval());
//            start = RECURRING_RULES.get(request.getRRule().getFrequency())
//                    .performOperation(start, request.getRRule().getInterval());
//
//            booking.setStartTime(start);
//            booking.setEndTime(end);
//        }
//        return booking;
//    }

    // Обновление данных

    /**
     * Обновляет отдельное бронирование на основе предоставленного запроса.
     *
     * @param request запрос с информацией для обновления бронирования
     * @throws UsernameNotFoundException пользователь с id, переданным клиентом, не найдена
     * @throws RoomNotFoundException     аудитория с id, переданным клиентом, не найдена
     */
    public Booking updateBooking(@NonNull RoomBookingRequestDTO request)
            throws UsernameNotFoundException, RoomNotFoundException, BookingConflictException {
        return updateBooking(getBookingFromDTO(request));
    }

    public Booking updateBooking(@NonNull Booking request)
            throws UsernameNotFoundException, RoomNotFoundException, BookingConflictException {

        if (!validateBooking(request.getStartTime(), request.getEndTime(), request.getRoom().getRoomId()))
            throw new BookingConflictException();

        return bookingRepository.save(request);
    }


    /**
     * Удаляет отдельное бронирование на основе предоставленного идентификатора.
     *
     * @param bookingId идентификатор бронирования для удаления
     * @throws BookingNotFoundException если бронирование не найдено по идентификатору
     */
    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }




    /**
     * @param dto - Дто запроса от клиента
     * @return объект для сохранения изменений в базу данных
     * @throws UsernameNotFoundException пользователь с указанным клиентом id не обнаружен
     * @throws RoomNotFoundException аудитория с указанным клиентом id не обнаружен
     */
    private Booking getBookingFromDTO(@NonNull RoomBookingRequestDTO dto)
            throws UsernameNotFoundException, RoomNotFoundException{
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException(dto.getRoomId()));

        return Booking.builder()
                .description(dto.getDescription())
                .owner(user)
                .room(room)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .id(dto.getId())
                .bookingGroupId(dto.getGroupBookingId())
                .staff(dto.getStaffId()
                        .stream()
                        .map(id -> userRepository.findById(id)
                                .orElseThrow(()->new UserNotFoundException(id)))
                        .toList())
                .groups(dto.getGroupsId()
                        .stream()
                        .map(id -> groupRepository.findById(id)
                                .orElseThrow(()->new GroupNotFoundException(id)))
                        .toList())
                .tag(dto.getTag())
                .build();
    }

    @FunctionalInterface
    private interface IncrementLocalDataTime {
        LocalDateTime performOperation(LocalDateTime inputDateTime, int value);
    }

    private boolean validateBooking(@NonNull LocalDateTime start,
                                    @NonNull LocalDateTime end,
                                    @NonNull Long roomId) {
        if (start.isAfter(end) || start.getDayOfYear() != end.getDayOfYear())
            return false;

        boolean isPresent = bookingRepository.findBookingsInDateRange(start, end)
                .stream()
                .anyMatch((bookingItem -> bookingItem.getRoom().getRoomId().equals(roomId)));
        return !isPresent;
    }
}