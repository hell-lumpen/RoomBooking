package org.mai.roombooking.repositories;

import org.mai.roombooking.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT room FROM Room room " +
            "WHERE room.capacity >= :capacity " +
            "AND room.hasComputers = :hasComputers "+
            "AND room.hasProjector = :hasProjector")
    List<Room> getRoomsWithFilter(@Param("capacity") int capacity,
                                                @Param("hasComputers") boolean hasComputers,
                                                @Param("hasProjector") boolean hasProjector);
}