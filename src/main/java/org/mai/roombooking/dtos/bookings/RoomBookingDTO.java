package org.mai.roombooking.dtos.bookings;

import lombok.*;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.entities.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomBookingDTO {
    private Long id;
    private UUID bookingGroupId;
    private String room;
    private String owner;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private Tag tag;

    public RoomBookingDTO(@NonNull Booking booking) {
        description = booking.getDescription();
        room = booking.getRoom().getRoomName();
        id = booking.getId();
        bookingGroupId = booking.getBookingGroupId();
        startTime = booking.getStartTime();
        endTime = booking.getEndTime();
        owner = booking.getOwner().getFullName();
        tag = booking.getTag();
    }
}
