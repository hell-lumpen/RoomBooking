package org.mai.roombooking.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.mai.roombooking.entities.Room;

@Data
public class RoomDTO {

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

    @NotNull Boolean isCathedral;

    public RoomDTO(@NonNull Room room) {
        this.id = room.getRoomId();
        this.name = room.getRoomName();
        this.capacity = room.getCapacity();
        this.hasComputers = room.getHasComputers();
        this.hasProjector = room.getHasProjector();
        this.isCathedral = room.getIsCathedral();
    }
    public RoomDTO(String name) {
        this.name = name;
        this.capacity = 0;
        this.hasComputers = false;
        this.hasProjector = false;
        this.isCathedral = false;
    }
}
