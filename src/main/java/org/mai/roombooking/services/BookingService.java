package org.mai.roombooking.services;

import lombok.NonNull;
import org.mai.roombooking.dtos.bookings.Pair;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingDetailsDTO;
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

    /**
     * Получение списка бронирований по идентификатору учебной группы
     * @param groupId идентификатор учебной группы
     * @return список бронирований, в которых участвует выбранная группа
     */
    public List<RoomBookingDTO> getBookingsByGroupId(Long groupId) {
        var group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
        return bookingRepository.findByGroupsContaining(group).stream().map(RoomBookingDTO::new).toList();
    }

    /**
     * Получение списка бронирований по названию учебной группы
     * @param groupName название учебной группы
     * @return список бронирований, в которых участвует выбранная группа
     */
    public List<RoomBookingDTO> getBookingsByGroupName(String groupName) {
        var group = groupRepository.findByName(groupName).orElseThrow();
        return bookingRepository.findByGroupsContaining(group).stream().map(RoomBookingDTO::new).toList();
    }

    /**
     * Получение списка бронирований по идентификатору участника (сотрудника)
     * @param staffId идентификатор сотрудника
     * @return список бронирований, в которых участвует выбранный сотрудник
     */
    public List<RoomBookingDTO> getBookingsByStaff(Long staffId) {
        var staff = userRepository.findById(staffId).orElseThrow(() -> new UserNotFoundException(staffId));
        return bookingRepository.findByStaffContaining(staff).stream().map(RoomBookingDTO::new).toList();
    }

    /**
     * Получение детализированной информации по запрашиваемому мероприятию
     * @param bookingId идентификатор бронирования
     * @return детализированная информация по бронированию
     * @throws BookingNotFoundException бронирование, с заданным идентификатором, не найдено
     */
    public RoomBookingDetailsDTO getBookingById(Long bookingId) throws BookingNotFoundException {
        return new RoomBookingDetailsDTO(bookingRepository.findById(bookingId)
                .orElseThrow(()->new BookingNotFoundException(bookingId)));
    }

    /**
     * Получение списка всех бронирований
     * @return список всех бронирований
     */
    public List<RoomBookingDTO> getAll() {
        return bookingRepository.findAll().stream().map(RoomBookingDTO::new).toList();
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

        if (request.getId() != null && bookingRepository.findById(request.getId()).isEmpty() &&
                !validateBooking(request.getStartTime(), request.getEndTime(), request.getRoom().getRoomId()))
            throw new BookingConflictException();

        return bookingRepository.save(request);
    }


    /**
     * Удаляет отдельное бронирование на основе предоставленного идентификатора.
     *
     * @param bookingId идентификатор бронирования для удаления
     */
    public void deleteBooking(@NonNull Long bookingId) {
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
        User user = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException(dto.getOwnerId()));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException(dto.getRoomId()));

        return Booking.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .owner(user)
                .room(room)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .id(dto.getId())
                .bookingGroupId(dto.getBookingGroupId())
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