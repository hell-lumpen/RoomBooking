package org.mai.roombooking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tag")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name", nullable = false, unique = true)
    String fullName;

    @Column(name = "short_name",nullable = false, unique = true)
    String shortName;
}
