package org.mai.roombooking.dtos.bookings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mai.roombooking.dtos.TagDTO;
import org.mai.roombooking.entities.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomBookingRequestDTO {
    private Long id;
    private UUID bookingGroupId;

    private Long ownerId;
    @NotNull
    private Long roomId;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
    @NotNull
    private String description;
    @NotNull
    private String title;
    @NotNull
    private List<Long> staffId;
    @NotNull
    private List<Long> groupsId;
    @NotNull
    private List<Long> tagsId;

    private Long recurringId;
    private Long recurringInterval;

    private String recurringUnit;

    private Long recurringCount;

    private LocalDateTime recurringEndDate;
}
