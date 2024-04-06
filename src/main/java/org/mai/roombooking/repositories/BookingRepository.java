package org.mai.roombooking.repositories;

import org.mai.roombooking.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b JOIN FETCH b.room JOIN FETCH b.owner " +
            " WHERE b.startTime <= :endDateTime AND " +
            "((b.endTime >= :startDateTime AND b.recurringRule IS NULL) OR " +
            "(b.recurringRule IS NOT NULL))")
    List<Booking> findBookingsInDateRange(@Param("startDateTime") LocalDateTime startDateTime,
                                          @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE " +
            " b.startTime <= :endDateTime" +
            " AND b.room.roomId = :roomId" +
            " AND ((b.endTime >= :startDateTime AND b.recurringRule IS NULL) OR " +
            " (b.recurringRule IS NOT NULL))")
    List<Booking> findBookingsInDateRangeForRoom(@Param("startDateTime") LocalDateTime startDateTime,
                                                 @Param("endDateTime") LocalDateTime endDateTime,
                                                 @Param("roomId") Long roomId);

    @Query("SELECT b FROM Booking b " +
            "WHERE " +
            " b.startTime <= :endDateTime " +
            " AND b.owner.id = :userId" +
            " AND ((b.endTime >= :startDateTime AND b.recurringRule IS NULL) OR " +
            " (b.recurringRule IS NOT NULL))")
    List<Booking> findBookingsInDateRangeByUser(@Param("startDateTime") LocalDateTime startDateTime,
                                                @Param("endDateTime") LocalDateTime endDateTime,
                                                @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Booking b WHERE b.bookingGroupId = :periodicBookingId")
    void deleteAllByPeriodicBookingId(@Param("periodicBookingId") UUID periodicBookingId);

    List<Booking> findAllByBookingGroupId(@Param("periodicBookingId") UUID periodicBookingId);

    @Query("select b FROM Booking b JOIN FETCH b.staff s WHERE s.id = :staffId " +
            "AND b.endTime >= :startDateTime " +
            "AND b.startTime <= :endDateTime")
    List<Booking> findByStaffContains(Long staffId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("select b FROM Booking b JOIN FETCH b.groups g WHERE g.id = :groupId " +
            "AND b.startTime <= :endDateTime"+
            " AND ((b.endTime >= :startDateTime AND b.recurringRule IS NULL) OR " +
            " (b.recurringRule IS NOT NULL))")
    List<Booking> findByGroupsContains(Long groupId, LocalDateTime startDateTime, LocalDateTime endDateTime);


    @Query("SELECT b FROM Booking b WHERE b.owner.id = :ownerId")
    List<Booking> findByOwner(Long ownerId);

    @Query("SELECT booking FROM Booking booking WHERE booking.status = :status")
    List<Booking> findBookingsByStatus(Booking.Status status);
}
