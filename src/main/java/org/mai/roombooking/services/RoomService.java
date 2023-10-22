//package org.mai.roombooking.services;
//
//
//import org.mai.roombooking.repositories.BookingRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
///**
// * Сервисный класс для управления комнатами.
// */
//@Service
//public class RoomService {
//
//    private final BookingRepository bookingRepository;
//
//    @Autowired
//    public RoomService(BookingRepository bookingRepository) {
//        this.bookingRepository = bookingRepository;
//    }
//
//    /**
//     * Получает список доступных комнат в заданном временном диапазоне с использованием опциональных параметров.
//     *
//     * @param startTime     дата и время начала запроса на доступность
//     * @param endTime       дата и время окончания запроса на доступность
//     * @param capacity      опциональный параметр вместимости для фильтрации комнат
//     * @param hasProjector  опциональный параметр проектора для фильтрации комнат
//     * @param hasComputers  опциональный параметр компьютеров для фильтрации комнат
//     * @return список доступных комнат, соответствующих указанным критериям
//     */
//    public List<RoomDTO> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime, Integer capacity, Boolean hasProjector, Boolean hasComputers) {
//        return null;
//    }
//
//    /**
//     * Получает список всех комнат со статусами, отсортированных по релевантности на основе опциональных параметров.
//     *
//     * @param capacity      опциональный параметр вместимости для фильтрации комнат
//     * @param hasProjector  опциональный параметр проектора для фильтрации комнат
//     * @param hasComputers  опциональный параметр компьютеров для фильтрации комнат
//     * @return список комнат со статусами
//     */
//    public List<RoomStatusDTO> getAllRoomsStatus(Integer capacity, Boolean hasProjector, Boolean hasComputers) {
//        return null;
//    }
//}