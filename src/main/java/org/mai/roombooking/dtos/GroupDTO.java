package org.mai.roombooking.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.entities.Tag;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {

    private Long id;

    @NotNull
    String name;

    public GroupDTO(@NonNull Group group) {
        id = group.getId();
        name = group.getName();
    }

}
