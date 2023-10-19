package org.mai.roombooking.services;

import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.RecurrentUnit;
import org.mai.roombooking.entities.dto.BookingDTO;
import org.mai.roombooking.entities.dto.BookingDetailsDTO;
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

    private List<BookingDTO> expandPeriodicBooking(Booking booking, LocalDateTime startTime, LocalDateTime endTime) {
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        LocalDateTime tmpStartTime = booking.getStartTime();
        LocalDateTime tmpEndTime = booking.getEndTime();

        Function<LocalDateTime, LocalDateTime> incrementRule = RECURRING_RULES.get(booking.getRecurringUnit());

        while (tmpEndTime.isBefore(booking.getRecurringEndTime())) {
//            if (tmpStartTime.isBefore(startTime) && tmpEndTime.isAfter(startTime) ||
//                    tmpStartTime.isBefore(endTime) && tmpEndTime.isAfter(endTime) ||
//                    tmpStartTime.isAfter(startTime) && tmpEndTime.isBefore(endTime) ||
//                    tmpStartTime.isBefore(startTime) && tmpEndTime.isAfter(endTime)
//            ){
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

    public List<BookingDTO> getBookingsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> bookingList = bookingRepository.findBookingsInDateRange(startTime, endTime);
        return convertBookingToDTOAndExpandPeriodic(startTime, endTime, bookingList);
    }

//    Метод возвращает список сокращенной информации о бронировании всех комнат во временном диапазоне
    public List<BookingDTO> getBookingsByTimeRangeOld(LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> bookingList = bookingRepository.findBookingsInDateRange(startTime, endTime);
        List<BookingDTO> bookingDTOList = new ArrayList<>();

        for (Booking booking : bookingList) {
            if (booking.getRecurringUnit() != null) {
                LocalDateTime tmpEndTime = booking.getEndTime();
                LocalDateTime tmpStartTime = booking.getEndTime();
                switch (booking.getRecurringUnit()) {
                    case DAILY:
                        while (tmpEndTime.isBefore(booking.getRecurringEndTime())) {
                            System.out.println(booking.getBookingId() + "\t" + tmpEndTime.toString() + " " + booking.getRecurringEndTime().toString());
                            bookingDTOList.add(BookingDTO.builder()
                                            .bookingId(booking.getBookingId())
                                            .roomName(booking.getRoom().getRoomName())
                                            .userFullName(booking.getUser().getFullName())
                                            .bookingPurpose(booking.getBookingPurpose())
                                            .isRecurrentBooking(true)
                                            .startTime(tmpStartTime)
                                            .endTime(tmpEndTime)
                                    .build());
                            tmpStartTime = tmpStartTime.plusDays(booking.getRecurringInterval());
                            tmpEndTime = tmpEndTime.plusDays(booking.getRecurringInterval());
                        }
                        break;
                    case WEEKLY:
                        while (tmpEndTime.isBefore(booking.getRecurringEndTime()) && tmpEndTime.isBefore(endTime)) {
                            System.out.println(booking.getBookingId() + "\t" + tmpEndTime.toString() + " " + booking.getRecurringEndTime().toString());
                            bookingDTOList.add(BookingDTO.builder()
                                    .bookingId(booking.getBookingId())
                                    .roomName(booking.getRoom().getRoomName())
                                    .userFullName(booking.getUser().getFullName())
                                    .bookingPurpose(booking.getBookingPurpose())
                                    .isRecurrentBooking(true)
                                    .startTime(tmpStartTime)
                                    .endTime(tmpEndTime)
                                    .build());
                            tmpStartTime = tmpStartTime.plusDays(7L * booking.getRecurringInterval());
                            tmpEndTime = tmpEndTime.plusDays(7L * booking.getRecurringInterval());
                        }
                        break;
                    case MONTHLY:
                        while (tmpEndTime.isBefore(booking.getRecurringEndTime()) && tmpEndTime.isBefore(endTime)) {
                            System.out.println(booking.getBookingId() + "\t" + tmpEndTime.toString() + " " + booking.getRecurringEndTime().toString());
                            bookingDTOList.add(BookingDTO.builder()
                                    .bookingId(booking.getBookingId())
                                    .roomName(booking.getRoom().getRoomName())
                                    .userFullName(booking.getUser().getFullName())
                                    .bookingPurpose(booking.getBookingPurpose())
                                    .isRecurrentBooking(true)
                                    .startTime(tmpStartTime)
                                    .endTime(tmpEndTime)
                                    .build());
                            tmpStartTime = tmpStartTime.plusMonths(booking.getRecurringInterval());
                            tmpEndTime = tmpEndTime.plusMonths(booking.getRecurringInterval());
                        }
                        break;
                    case YEARLY:
                        while (tmpEndTime.isBefore(booking.getRecurringEndTime()) && tmpEndTime.isBefore(endTime)) {
                            bookingDTOList.add(BookingDTO.builder()
                                    .bookingId(booking.getBookingId())
                                    .roomName(booking.getRoom().getRoomName())
                                    .userFullName(booking.getUser().getFullName())
                                    .bookingPurpose(booking.getBookingPurpose())
                                    .isRecurrentBooking(true)
                                    .startTime(tmpStartTime)
                                    .endTime(tmpEndTime)
                                    .build());
                            tmpStartTime = tmpStartTime.plusYears(booking.getRecurringInterval());
                            tmpEndTime = tmpEndTime.plusYears(booking.getRecurringInterval());
                        }
                        break;
                }
            }
            else {
                bookingDTOList.add(booking.toDTO());
            }
        }

        return bookingDTOList;
    }

//    Метод возвращает список сокращенной информации о бронировании по названию комнаты во временном диапазоне
    public List<BookingDTO> getBookingsByRoomAndTimeRange(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> bookingList = bookingRepository.findBookingsInDateRangeForRoom(startTime, endTime, roomId);
        return convertBookingToDTOAndExpandPeriodic(startTime, endTime, bookingList);


    }

    private List<BookingDTO> convertBookingToDTOAndExpandPeriodic(LocalDateTime startTime, LocalDateTime endTime, List<Booking> bookingList) {
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

    //    Метод возвращает расширенную информацию о бронировании (правило периодичности если есть)
    public BookingDetailsDTO getBookingDetailsById(Long bookingId) {
        return null;
    }

    public Booking saveBooking(Booking booking) {
        bookingRepository.save(booking);
//        List<Booking> list = bookingRepository.findBookingsInDateRange(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        return null;
    }
}
