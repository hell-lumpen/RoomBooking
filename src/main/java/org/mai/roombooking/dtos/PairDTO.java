package org.mai.roombooking.dtos;

import lombok.Data;
import lombok.NonNull;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.entities.Room;
import org.mai.roombooking.entities.User;

@Data
public class PairDTO {

    private long id;
    private String value;

    public PairDTO(@NonNull User user) {
        id = user.getUserId();
        value = user.getFullName();
    }

    public PairDTO(@NonNull Room room) {
        id = room.getRoomId();
        value = room.getRoomName();
    }

    public PairDTO(@NonNull Group group) {
        id = group.getId();
        value = group.getName();
    }
}
