package org.mai.roombooking.entities;

import jakarta.persistence.*;
import lombok.*;
import org.mai.roombooking.dtos.TagDTO;

import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name", nullable = false, unique = true)
    String fullName;

    @Column(name = "short_name",nullable = false, unique = true)
    String shortName;

    @Column(name = "color", nullable = false)
    String color;

    public Tag(@NonNull TagDTO dto) {
        id = dto.getId();
        fullName = dto.getFullName();
        shortName = dto.getShortName();
        color = dto.getColor();
    }
    public Tag(String _shortName, String _fullName, String _color) {
        shortName = _shortName;
        fullName = _fullName;
        color = _color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(fullName, tag.fullName) &&
                Objects.equals(shortName, tag.shortName) && Objects.equals(color, tag.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, shortName, color);
    }
}
