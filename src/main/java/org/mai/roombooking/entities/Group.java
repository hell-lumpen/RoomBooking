package org.mai.roombooking.entities;

import jakarta.persistence.*;
import lombok.*;
import org.mai.roombooking.dtos.GroupDTO;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int size;


    public Group(@NonNull GroupDTO dto) {
        id = dto.getId();
        name = dto.getName();
        size = 25;
    }

    /**
     * Метод получения факультета группы
     * @return факультет
     */
    public int getFaculty() {
        return (int) name.charAt(1);
    }

    /**
     * Метод получения текущего курса группы
     * @return текущий курс
     */
    public int getCourse() {
        return (int) name.charAt(4);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}