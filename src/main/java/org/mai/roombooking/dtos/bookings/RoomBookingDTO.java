package org.mai.roombooking.dtos.bookings;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    private UserDTO owner;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    private LocalDateTime startTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    private LocalDateTime endTime;
    private String title;
    private String description;
    private List<TagDTO> tags;
    private List<GroupDTO> groups;
    private List<UserDTO> staff;
    private Booking.Status status;
    private Long recurringId = null;
    private Long recurringInterval = null;
    private String recurringUnit = null;
    private Long recurringCount = null;
    private LocalDateTime recurringEndDate = null;

    public RoomBookingDTO(@NonNull Booking booking) {
        title = booking.getTitle();
        room = new PairDTO(booking.getRoom());
        id = booking.getId();
        bookingGroupId = booking.getBookingGroupId();
        startTime = booking.getStartTime();
        endTime = booking.getEndTime();
        owner = new UserDTO(booking.getOwner());
        tags = booking.getTags().stream().map(TagDTO::new).toList();
        description = booking.getDescription();
        groups = booking.getGroups().stream().map(GroupDTO::new).toList();
        staff = booking.getStaff().stream().map(UserDTO::new).toList();
        status = booking.getStatus();
        if  (booking.getRecurringRule() != null) {
            recurringId = booking.getRecurringRule().getId();
            recurringInterval = booking.getRecurringRule().getInterval();
            recurringUnit = booking.getRecurringRule().getUnit();
            recurringCount = booking.getRecurringRule().getCount();
            recurringEndDate = booking.getRecurringRule().getEndTime();
        }
    }
}
