package org.mai.roombooking.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
@Entity
@NoArgsConstructor
@Getter
@Table(name = "equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private Equipment.Type type;

    @Column(name = "inv_num")
    private Long invNum;

    public enum Type {Laptop, Project, VR}

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room")
    private Room room;

    public Equipment(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return Objects.equals(id, equipment.id) && Objects.equals(name, equipment.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
