package org.mai.roombooking.services;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomService {
    public List<RoomDTO> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime, Integer capacity, Boolean hasProjector, Boolean hasComputers) {
        return null;
    }

    public List<RoomStatusDTO> getAllRoomsStatus(Integer capacity, Boolean hasProjector, Boolean hasComputers) {
        return null;
    }
}
