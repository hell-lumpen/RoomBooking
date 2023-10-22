package org.mai.roombooking.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mai.roombooking.entities.RRule;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomBookingRequestDTO {
    private Long id;
    private UUID periodicBookingId;
    private Long roomId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private RRule rRule;

    public Boolean isPeriodic() {
        return rRule != null;
    }
}
