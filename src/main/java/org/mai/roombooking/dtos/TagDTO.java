package org.mai.roombooking.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mai.roombooking.entities.Tag;
import org.springframework.lang.NonNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    @NotNull
    private Long id;

    @NotNull
    private String fullName;
    @NotNull
    private String shortName;

    @NotNull
    @Pattern(regexp = "^#[0-9A-Fa-f]+$")
    private String color;

    public TagDTO(@NonNull Tag tag) {
        id        = tag.getId();
        fullName  = tag.getFullName();
        shortName = tag.getShortName();
        color     = tag.getColor();
    }
}
