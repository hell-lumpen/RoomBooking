package org.mai.roombooking.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.mai.roombooking.dtos.PairDTO;
import org.mai.roombooking.dtos.bookings.Pair;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingDetailsDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingRequestDTO;
import org.mai.roombooking.entities.*;
import org.mai.roombooking.exceptions.*;
import org.mai.roombooking.repositories.BookingRepository;
import org.mai.roombooking.exceptions.base.BookingException;
import org.mai.roombooking.repositories.*;
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
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final GroupRepository groupRepository;
    private final TagRepository tagRepository;
    private final ValidationService validationService;

    private final RecurringRuleRepository recurringRuleRepository;
    private final RecurringExceptionRepository recurringExceptionRepository;
    private final RuleExceptionRepository ruleExceptionRepository;

    // Получение данных

//    /**
//     * Получение списка бронирований по идентификатору учебной группы
//     * @param groupId идентификатор учебной группы
//     * @return список бронирований, в которых участвует выбранная группа
//     */
//    public List<RoomBookingDTO> getBookingsByGroupId(Long groupId) {
//        var group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
//        return bookingRepository.findByGroupsContaining(group).stream().map(RoomBookingDTO::new).toList();
//    }
//
//
//    /**
//     * Получение списка бронирований по идентификатору участника (сотрудника)
//     * @param staffId идентификатор сотрудника
//     * @return список бронирований, в которых участвует выбранный сотрудник
//     */
//    public List<RoomBookingDTO> getBookingsByStaff(Long staffId) {
//        var staff = userRepository.findById(staffId).orElseThrow(() -> new UserNotFoundException(staffId));
//        return bookingRepository.findByStaffContaining(staff).stream().map(RoomBookingDTO::new).toList();
//    }

    public List<Booking> getBookingsByStatus(Booking.Status status) {
        return bookingRepository.findByStatus(status);
    }

    public List<Booking> getLastBookingsByOwner(Long ownerId, int limit) {
        return bookingRepository.findByOwner(ownerId).stream()
                .sorted(Comparator.comparing(Booking::getStartTime))
                .limit(limit)
                .toList();
    }

    /**
     * Получение детализированной информации по запрашиваемому мероприятию
     *
     * @param bookingId идентификатор бронирования
     * @return детализированная информация по бронированию
     * @throws BookingNotFoundException бронирование, с заданным идентификатором, не найдено
     */
    public RoomBookingDetailsDTO getBookingById(Long bookingId) throws BookingNotFoundException {
        return new RoomBookingDetailsDTO(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId)));
    }

    /**
     * Получение списка всех бронирований
     *
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
        List<Booking> bookings = bookingRepository.findBookingsInDateRange(startTime, endTime);

        Map<PairDTO, List<RoomBookingDTO>> groupedBookings =
                validationService.checkBookingRecurring(bookings, startTime, endTime, true)
                        .stream()
                        .map((RoomBookingDTO::new))
                        .collect(Collectors.groupingBy(RoomBookingDTO::getRoom, Collectors.toList()));

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
    public List<RoomBookingDTO> getBookingsByRoomInTimeRange(Long roomId, LocalDateTime startTime,
                                                             LocalDateTime endTime) {
        List<Booking> bookings = bookingRepository.findBookingsInDateRangeForRoom(startTime, endTime, roomId);
        return validationService.checkBookingRecurring(bookings, startTime, endTime, true)
                .stream().map((RoomBookingDTO::new)).toList();
    }

    /**
     * Получает бронирования для конкретного пользователя в заданном временном диапазоне.
     *
     * @param userId    идентификатор пользователя
     * @param startTime дата и время начала запроса
     * @param endTime   дата и время окончания запроса
     * @return список бронирований для указанного пользователя в заданном временном диапазоне
     * @throws UsernameNotFoundException если пользователь не найден по идентификатору
     * @throws AccessDeniedException     если пользователю недостаточно прав для выполнения запроса
     */
    public List<RoomBookingDTO> getBookingsByUserInTimeRange(Long userId,
                                                             LocalDateTime startTime,
                                                             LocalDateTime endTime)
            throws UsernameNotFoundException {
        List<Booking> bookings = bookingRepository.findBookingsInDateRangeByUser(startTime, endTime, userId);
        if (bookings.isEmpty())
            userRepository.findById(userId).orElseThrow(() ->
                    new UsernameNotFoundException("User with id" + userId + "not found"));

        return validationService.checkBookingRecurring(bookings, startTime, endTime, true)
                .stream().map((RoomBookingDTO::new)).toList();
    }

    public List<Booking> getBookingsByGroupInTimeRange(Long groupId,
                                                       LocalDateTime startTime,
                                                       LocalDateTime endTime) {
        return bookingRepository.findByGroupsContains(groupId, startTime, endTime);
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
            throws UsernameNotFoundException, RoomNotFoundException, BookingException {


        return updateBooking(getBookingFromDTO(request));
    }

    public Booking updateBooking(@NonNull Booking request)
            throws UsernameNotFoundException, RoomNotFoundException, BookingException {

        validationService.validateBooking(request.getStartTime(), request.getEndTime(), request.getRoom().getRoomId(), request.getId());
        if (request.getRecurringRule() != null && request.getId() == null)
            recurringRuleRepository.save(request.getRecurringRule());
        else if (request.getRecurringRule() != null) {
            RecurringRule rule = request.getRecurringRule();
            RecurringException exception;
            if (rule.getExceptions() == null){
                rule.setExceptions(new HashSet<>());
            }
            exception = RecurringException.builder().date(request.getStartTime().toLocalDate()).build();
            exception = recurringExceptionRepository.save(exception);
            ruleExceptionRepository.insertWithQuery(rule.getId(), exception.getId());

            request.setId(null);
            request.setRecurringRule(null);
            return bookingRepository.save(request);
        }

        return bookingRepository.save(request);
    }

    public Booking updateBooking(@NonNull Booking request, boolean validation)
            throws UsernameNotFoundException, RoomNotFoundException, BookingException {
        if (validation)
            validationService.validateBooking(request.getStartTime(), request.getEndTime(), request.getRoom().getRoomId(), request.getId());
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
     * @throws RoomNotFoundException     аудитория с указанным клиентом id не обнаружен
     */
    private Booking getBookingFromDTO(@NonNull RoomBookingRequestDTO dto)
            throws UsernameNotFoundException, RoomNotFoundException {
        User user = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException(dto.getOwnerId()));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException(dto.getRoomId()));

        RecurringRule recurringRule = null;
        if (dto.getRecurringInterval() != null) {
            recurringRule = RecurringRule.builder()
                    .id(dto.getRecurringId())
                    .count(dto.getRecurringCount())
                    .unit(dto.getRecurringUnit())
                    .endTime(dto.getEndTime())
                    .interval(dto.getRecurringInterval())
                    .build();
        }

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
                                .orElseThrow(() -> new UserNotFoundException(id)))
                        .toList())
                .groups(dto.getGroupsId()
                        .stream()
                        .map(id -> groupRepository.findById(id)
                                .orElseThrow(() -> new GroupNotFoundException(id)))
                        .collect(Collectors.toSet()))
                .tags(dto.getTagsId().stream().map((id) -> tagRepository.findById(id).orElseThrow(() ->
                        new TagNotFoundException("tag whis id: " + id))).collect(Collectors.toSet()))
                .recurringRule(recurringRule)
                .status(dto.getStatus())
                .build();
    }


    private void validateBooking(@NonNull LocalDateTime start,
                                 @NonNull LocalDateTime end,
                                 @NonNull Long roomId) throws BookingException {
        if (start.isAfter(end) || start.getDayOfYear() != end.getDayOfYear())
            throw new BookingException("Booking exception");

        var conflicts = bookingRepository.findBookingsInDateRange(start, end)
                .stream()
                .filter((bookingItem -> bookingItem.getRoom().getRoomId().equals(roomId)))
                .map(RoomBookingDTO::new)
                .toList();

        if (!conflicts.isEmpty())
            throw new BookingConflictException(conflicts);
    }
}