package org.mai.roombooking.repositories;

import org.mai.roombooking.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "WHERE b.endTime >= :startDateTime AND b.startTime <= :endDateTime")
    List<Booking> findBookingsInDateRange(@Param("startDateTime") LocalDateTime startDateTime,
                                          @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.endTime >= :startDateTime " +
            "   AND b.startTime <= :endDateTime" +
            "   AND b.room.roomId = :roomId")
    List<Booking> findBookingsInDateRangeForRoom(@Param("startDateTime") LocalDateTime startDateTime,
                                                 @Param("endDateTime") LocalDateTime endDateTime,
                                                 @Param("roomId") Long roomId);


    @Query("SELECT b.room.roomName, COLLECT_LIST(b) FROM Booking b GROUP BY b.room.roomName HAVING b.endTime >= :startDateTime AND b.startTime <= :endDateTime ")
    Map<String, List<Booking>> findBookingsInDateRangeAndGroupByRoomName (
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
}