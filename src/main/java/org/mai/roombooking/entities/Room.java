package org.mai.roombooking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "Rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_name", nullable = false, unique = true)
    private String roomName;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "has_computers", nullable = false, columnDefinition = "boolean default false")
    private Boolean hasComputers;

    @Column(name = "has_projector", nullable = false, columnDefinition = "boolean default false")
    private Boolean hasProjector;

    @Column(name = "is_cathedral", nullable = false, columnDefinition = "boolean default false")
    private Boolean isCathedral;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomId, room.roomId) && Objects.equals(roomName, room.roomName) &&
                Objects.equals(capacity, room.capacity) && Objects.equals(hasComputers, room.hasComputers)
                && Objects.equals(hasProjector, room.hasProjector) && Objects.equals(isCathedral, room.isCathedral);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, roomName, capacity, hasComputers, hasProjector, isCathedral);
    }
}