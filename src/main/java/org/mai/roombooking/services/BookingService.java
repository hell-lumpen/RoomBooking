package org.mai.roombooking.services;

import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.RecurrentUnit;
import org.mai.roombooking.entities.dto.BookingDTO;
import org.mai.roombooking.entities.dto.BookingDetailsDTO;
import org.mai.roombooking.exceptions.BookingNotFoundException;
import org.mai.roombooking.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Группирует и сортирует список бронирований по указанному полю и возвращает результат в виде Map.
     *
     * @param bookings    Список бронирований для обработки.
     * @param groupByField Функция для группировки бронирований по указанному полю.
     * @param sortByField  Функция для определения поля, по которому производится сортировка.
     * @param ascending    Флаг, указывающий, следует ли сортировать по возрастанию (true) или убыванию (false).
     * @param <T>          Тип, реализующий интерфейс Comparable, для определения поля сортировки.
     * @return Map, где ключ - это значение поля groupByField, а значение - список бронирований,
     *         отсортированный по указанному полю.
     * <p>
     * // Пример 1: Группировка по roomName и сортировка по startTime по возрастанию
     *         Map<String, List<BookingDTO>> result1 = bookingService.groupAndSortBookings(
     *                 yourBookingsList,
     *                 BookingDTO::getRoomName,
     *                 BookingDTO::getStartTime,
     *                 true
     *         );
     *
     *         // Пример 2: Группировка по userFullName и сортировка по endTime по убыванию
     *         Map<String, List<BookingDTO>> result2 = bookingService.groupAndSortBookings(
     *                 yourBookingsList,
     *                 BookingDTO::getUserFullName,
     *                 BookingDTO::getEndTime,
     *                 false
     *         );
     * </p>
     */
    public <T extends Comparable<? super T>> Map<String, List<BookingDTO>> groupAndSortBookings(
            List<BookingDTO> bookings,
            Function<BookingDTO, String> groupByField,
            Function<BookingDTO, T> sortByField,
            boolean ascending
    ) {
        return bookings.stream()
                .collect(Collectors.groupingBy(groupByField,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(ascending
                                                ? Comparator.comparing(sortByField)
                                                : Comparator.comparing(sortByField).reversed())
                                        .toList()
                        )
                ));
    }

    private static final Map<RecurrentUnit, Function<LocalDateTime, LocalDateTime>> RECURRING_RULES = new EnumMap<>(RecurrentUnit.class);

    static {
        RECURRING_RULES.put(RecurrentUnit.DAILY, dateTime -> dateTime.plusDays(1));
        RECURRING_RULES.put(RecurrentUnit.WEEKLY, dateTime -> dateTime.plusWeeks(1));
        RECURRING_RULES.put(RecurrentUnit.MONTHLY, dateTime -> dateTime.plusMonths(1));
        RECURRING_RULES.put(RecurrentUnit.YEARLY, dateTime -> dateTime.plusYears(1));
    }

    /**
     * Раскрывает периодическое бронирование на отдельные разовые бронирования в заданном временном диапазоне.
     *
     * @param booking    периодическое бронирование, подлежащее раскрытию
     * @param startTime  начальное время диапазона
     * @param endTime    конечное время диапазона
     * @return список DTO для разовых бронирований, полученных из раскрытия периодического бронирования
     */
    private List<BookingDTO> expandPeriodicBooking(Booking booking, LocalDateTime startTime, LocalDateTime endTime) {
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        LocalDateTime tmpStartTime = booking.getStartTime();
        LocalDateTime tmpEndTime = booking.getEndTime();

        Function<LocalDateTime, LocalDateTime> incrementRule = RECURRING_RULES.get(booking.getRecurringUnit());

        while (tmpEndTime.isBefore(booking.getRecurringEndTime())) {
            if (!tmpEndTime.isBefore(startTime) && !tmpStartTime.isAfter(endTime)) {
                bookingDTOList.add(BookingDTO.builder()
                        .bookingId(booking.getBookingId())
                        .roomName(booking.getRoom().getRoomName())
                        .userFullName(booking.getUser().getFullName())
                        .bookingPurpose(booking.getBookingPurpose())
                        .isRecurrentBooking(true)
                        .startTime(tmpStartTime)
                        .endTime(tmpEndTime)
                        .build());
            }
            tmpStartTime = incrementRule.apply(tmpStartTime);
            tmpEndTime = incrementRule.apply(tmpEndTime);
        }

        return bookingDTOList;
    }

    /**
     * Получает список бронирований в заданном временном диапазоне и конвертирует их в список DTO.
     * Раскрывает периодические бронирования в виде списка разовых бронирований.
     *
     * @param startTime начальное время диапазона
     * @param endTime   конечное время диапазона
     * @return List<BookingDTO> бронирований в указанном временном диапазоне
     */
    public List<BookingDTO> getBookingsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> bookingList = bookingRepository.findBookingsInDateRange(startTime, endTime);
        return convertBookingToDTOAndExpandPeriodic(startTime, endTime, bookingList);
    }

    /**
     * Осуществляет поиск информации о бронировании по названию комнаты в заданном временном диапазоне и конвертирует их в список DTO.
     * Раскрывает периодические бронирования в виде списка разовых бронирований.
     *
     * @param roomId     идентификатор комнаты
     * @param startTime  начальное время диапазона
     * @param endTime    конечное время диапазона
     * @return List<BookingDTO> бронирований в указанной комнате и временном диапазоне
     */
    public List<BookingDTO> getBookingsByRoomAndTimeRange(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> bookingList = bookingRepository.findBookingsInDateRangeForRoom(startTime, endTime, roomId);
        return convertBookingToDTOAndExpandPeriodic(startTime, endTime, bookingList);
    }

    /**
     * Конвертирует список бронирований в список. Раскрывает периодические бронирования в виде списка разовых
     * бронирований с помощью метода {@link #expandPeriodicBooking(Booking, LocalDateTime, LocalDateTime)}.
     *
     * @param startTime    начальное время диапазона
     * @param endTime      конечное время диапазона
     * @param bookingList  список бронирований для конвертации и раскрытия
     * @return список DTO для бронирований с учетом периодичности
     */
    private List<BookingDTO> convertBookingToDTOAndExpandPeriodic(LocalDateTime startTime,
                                                                  LocalDateTime endTime,
                                                                  List<Booking> bookingList) {
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        for (Booking booking : bookingList) {
            if (booking.getRecurringUnit() != null) {
                bookingDTOList.addAll(expandPeriodicBooking(booking, startTime, endTime));
            } else {
                bookingDTOList.add(booking.toDTO());
            }
        }

        return bookingDTOList;
    }

    /**
     * Возвращает расширенную информацию о бронировании, включая правило периодичности, если оно установлено.
     *
     * @param bookingId идентификатор бронирования
     * @return BookingDetailsDTO с расширенной информацией о бронировании
     * @throws BookingNotFoundException если бронирование с указанным идентификатором не найдено
     */
    public BookingDetailsDTO getBookingDetailsById(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return booking.map(Booking::toDetailsDTO)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }
}
