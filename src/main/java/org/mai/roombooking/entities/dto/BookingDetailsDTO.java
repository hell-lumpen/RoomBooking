package org.mai.roombooking.entities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDetailsDTO {
    private Long bookingId;
    private RoomDTO room;
    private UserDTO user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String bookingPurpose;
    private Integer recurringInterval;
    private String recurringUnit;
    private LocalDateTime recurrentEndTime;
}

