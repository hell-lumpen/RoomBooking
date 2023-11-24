package org.mai.roombooking.dtos.bookings;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.mai.roombooking.dtos.GroupDTO;
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
    private String title;
    private String description;
    private Tag tag;

    public RoomBookingDTO(@NonNull Booking booking) {
        title = booking.getTitle();
        room = booking.getRoom().getRoomName();
        id = booking.getId();
        bookingGroupId = booking.getBookingGroupId();
        startTime = booking.getStartTime();
        endTime = booking.getEndTime();
        owner = booking.getOwner().getFullName();
        tag = booking.getTag();
        description = booking.getDescription();
    }
}
