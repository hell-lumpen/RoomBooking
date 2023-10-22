package org.mai.roombooking.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.RRule;
import org.mai.roombooking.entities.Room;
import org.mai.roombooking.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomBookingDetailsDTO {
    private Long id;
    private UUID periodicBookingId;
    private Room room;
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String bookingPurpose;
    private RRule rRule;
    private LocalDateTime createdAt;

    public RoomBookingDetailsDTO(Booking booking) {
        id = booking.getId();
        periodicBookingId = booking.getPeriodicBookingId();
        room = booking.getRoom();
        user = booking.getUser();
        startTime = booking.getStartTime();
        endTime = booking.getEndTime();
        bookingPurpose = booking.getBookingPurpose();
        rRule = booking.getRRule();
        createdAt = booking.getCreatedAt();
    }
}
