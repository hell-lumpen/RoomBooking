package org.mai.roombooking.dtos;

import lombok.Data;
import lombok.NonNull;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.entities.Room;
import org.mai.roombooking.entities.User;

@Data
public class dPairDTO {

    private long id;
    private String value;

    public PairDTO(@NonNull User user) {
        id = user.getId();
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
