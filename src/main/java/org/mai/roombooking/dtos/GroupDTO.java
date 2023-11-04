package org.mai.roombooking.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.mai.roombooking.entities.Group;

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
