package org.mai.roombooking.dtos.bookings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.entities.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomBookingRequestDTO {
    private Long id;
    private UUID groupBookingId;

    @NotNull
    private Long roomId;
    @NotNull
    private Long userId;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private List<Long> staffId;
    @NotNull
    private List<Long> groupsId;
    @NotNull
    private Tag tag;
}
