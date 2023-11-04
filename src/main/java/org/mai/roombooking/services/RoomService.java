package org.mai.roombooking.services;


import org.mai.roombooking.dtos.RoomDTO;
import org.mai.roombooking.entities.Room;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.repositories.BookingRepository;
import org.mai.roombooking.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервисный класс для управления комнатами.
 */
@Service
public class RoomService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    @Autowired
    public RoomService(BookingRepository bookingRepository,
                       RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * Получает список доступных комнат в заданном временном диапазоне с использованием опциональных параметров.
     *
     * @param startTime     дата и время начала запроса на доступность
     * @param endTime       дата и время окончания запроса на доступность
     * @param capacity      опциональный параметр вместимости для фильтрации комнат
     * @param hasProjector  опциональный параметр проектора для фильтрации комнат
     * @param hasComputers  опциональный параметр компьютеров для фильтрации комнат
     * @return список доступных комнат, соответствующих указанным критериям, если он пуст доступных комнат нет
     */
    public List<RoomDTO> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime,
                                           Integer capacity, Boolean hasProjector, Boolean hasComputers) {

        List<Room> rooms = bookingRepository.findBookingsInDateRange(startTime, endTime)
                    .stream()
                    .map((Booking::getRoom))
                    .distinct()
                    .toList();

        return roomRepository.findAll().stream()
                .filter((room -> capacity==null || room.getCapacity() > capacity))
                .filter(room -> hasComputers == null || hasComputers == room.getHasComputers())
                .filter(room -> hasProjector == null || hasProjector == room.getHasProjector())
                .filter(element -> !rooms.contains(element))
                .map((RoomDTO::new))
                .toList();
    }

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream().map(RoomDTO::new).toList();
    }

    public RoomDTO update(RoomDTO dto) {
        roomRepository.save(getRoomFromDTO(dto));
        return null;
    }

    public List<RoomDTO> getCathedralRooms() {
        return roomRepository.getCathedralRooms().stream().map(RoomDTO::new).toList();
    }

    public void delete(@NonNull Long roomId) {
        roomRepository.deleteById(roomId);
    }

    private Room getRoomFromDTO(@NonNull RoomDTO dto) {
        return Room.builder()
                .roomName(dto.getName())
                .roomId(dto.getId())
                .capacity(dto.getCapacity())
                .hasComputers(dto.getHasComputers())
                .hasProjector(dto.getHasProjector())
                .build();
    }
}