package org.mai.roombooking.dtos.bookings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private UserDTO user ;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private LocalDateTime createdAt;
    private List<UserDTO> staff;
    private List<Group> groups;

    public RoomBookingDetailsDTO(Booking booking) {
        id = booking.getId();
        bookingGroupId = booking.getBookingGroupId();
        room = booking.getRoom();
        user = new UserDTO(booking.getOwner());
        startTime = booking.getStartTime();
        endTime = booking.getEndTime();
        description = booking.getDescription();
        staff = booking.getStaff().stream().map(UserDTO::new).toList();
        groups = booking.getGroups();
        createdAt = booking.getCreatedAt();
    }
}
