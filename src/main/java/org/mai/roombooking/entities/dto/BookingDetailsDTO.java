package org.mai.roombooking.entities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.mai.roombooking.entities.RecurrentUnit;
import org.mai.roombooking.entities.Room;
import org.mai.roombooking.entities.User;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDetailsDTO {
    private Long bookingId;
    private Room room;
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String bookingPurpose;
    private Integer recurringInterval;
    private RecurrentUnit recurringUnit;
    private LocalDateTime recurrentEndTime;
}

