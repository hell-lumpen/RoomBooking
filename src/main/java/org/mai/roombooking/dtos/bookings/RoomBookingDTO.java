package org.mai.roombooking.dtos.bookings;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.mai.roombooking.dtos.GroupDTO;
import org.mai.roombooking.dtos.PairDTO;
import org.mai.roombooking.dtos.TagDTO;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.entities.Tag;
import org.mai.roombooking.entities.UserInfo;

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
    private PairDTO room;
    private PairDTO owner;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String description;
    private List<TagDTO> tags;
    private List<UserDTO> staff;
    private List<GroupDTO> groups;
    private String status;

    public RoomBookingDTO(@NonNull Booking booking) {
        id = booking.getId();
        bookingGroupId = booking.getBookingGroupId();
        title = booking.getTitle();
        description = booking.getDescription();
        room = new PairDTO(booking.getRoom());
        owner = new PairDTO(booking.getOwner());
        startTime = booking.getStartTime();
        endTime = booking.getEndTime();
        staff = booking.getStaff().stream().map(UserDTO::new).toList();
        groups = booking.getGroups().stream().map(GroupDTO::new).toList();
        tags = booking.getTags().stream().map(TagDTO::new).toList();
        status = booking.getStatus().name();
    }
}
