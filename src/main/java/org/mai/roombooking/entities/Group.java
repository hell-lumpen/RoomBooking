package org.mai.roombooking.entities;

import jakarta.persistence.*;
import lombok.*;
import org.mai.roombooking.dtos.GroupDTO;

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

    @Column(nullable = false)
    String name;

    public Group(@NonNull GroupDTO dto) {
        id = dto.getId();
        name = dto.getName();
    }
}