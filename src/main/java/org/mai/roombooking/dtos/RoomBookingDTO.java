package org.mai.roombooking.dtos;

import lombok.*;
import org.mai.roombooking.entities.Booking;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomBookingDTO {
    private Long id;
    private UUID periodicBookingId;
    private String room;
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;

    public RoomBookingDTO(Booking booking) {
        description = booking.getBookingPurpose();
        room = booking.getRoom().getRoomName();
        id = booking.getId();
        periodicBookingId = booking.getPeriodicBookingId();
        startTime = booking.getStartTime();
        endTime = booking.getEndTime();
        username = booking.getUser().getFullName();
    }
}
