package org.mai.roombooking.dtos;

import lombok.NonNull;
import org.mai.roombooking.entities.Room;

public class RoomDTO {
    Long id;
    String name;

    public RoomDTO(@NonNull Room room) {
        this.id = room.getRoomId();
        this.name = room.getRoomName();
    }
}
