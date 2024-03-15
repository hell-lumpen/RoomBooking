package org.mai.roombooking.dtos.bookings;

import lombok.*;
import org.mai.roombooking.dtos.GroupDTO;
import org.mai.roombooking.dtos.RoomDTO;
import org.mai.roombooking.dtos.TagDTO;
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
    private RoomDTO room;
    private UserDTO owner;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String description;
    private List<UserDTO> staff;
    private List<GroupDTO> groups;
    private List<TagDTO> tags;
    private String status;

    public RoomBookingDetailsDTO(@NonNull Booking booking) {
        id = booking.getId();
        bookingGroupId = booking.getBookingGroupId();
        title = booking.getTitle();
        room = new RoomDTO(booking.getRoom());
        owner = new UserDTO(booking.getOwner());
        startTime = booking.getStartTime();
        endTime = booking.getEndTime();
        description = booking.getDescription();
        staff = booking.getStaff().stream().map(UserDTO::new).toList();
        groups = booking.getGroups().stream().map(GroupDTO::new).toList();
        tags = booking.getTags().stream().map(TagDTO::new).toList();
        status = booking.getStatus().name();
    }
}
