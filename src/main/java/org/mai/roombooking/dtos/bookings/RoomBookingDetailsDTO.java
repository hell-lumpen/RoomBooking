package org.mai.roombooking.dtos.bookings;

import lombok.*;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.entities.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomBookingDetailsDTO {
    private Long id;
    private UUID bookingGroupId ;
    private Room room;
    private UserDTO owner;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String description;
    private List<UserDTO> staff;
    private List<Group> groups;
    private Tag tag;

    public RoomBookingDetailsDTO(@NonNull Booking booking) {
        id = booking.getId();
        bookingGroupId = booking.getBookingGroupId();
        title = booking.getTitle();
        room = booking.getRoom();
        owner = new UserDTO(booking.getOwner());
        startTime = booking.getStartTime();
        endTime = booking.getEndTime();
        description = booking.getDescription();
        staff = booking.getStaff().stream().map(UserDTO::new).toList();
        groups = booking.getGroups();
        tag = booking.getTag();
    }
}
