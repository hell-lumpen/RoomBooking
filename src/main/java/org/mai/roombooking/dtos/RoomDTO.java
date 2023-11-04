package org.mai.roombooking.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.mai.roombooking.entities.Room;

@Data
public class RoomDTO {
    @NotNull
    Long id;

    @NotNull
    @NotBlank
    String name;

    @NotNull
    private Integer capacity;

    @NotNull
    private Boolean hasComputers;

    @NotNull
    private Boolean hasProjector;

    public RoomDTO(@NonNull Room room) {
        this.id = room.getRoomId();
        this.name = room.getRoomName();
        this.capacity = room.getCapacity();
        this.hasComputers = room.getHasComputers();
        this.hasProjector = room.getHasProjector();
    }
}
