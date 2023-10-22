package org.mai.roombooking.dtos;

import lombok.Builder;
import lombok.Getter;
import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.Room;

import java.awt.print.Book;
import java.time.LocalDateTime;

@Builder
@Getter
public class RoomBookingDTO {
    private Long id;
    private Long periodicBookingId;
    private String room;
    private String employeeName;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private String description;

    public RoomBookingDTO(Booking booking) {
        description = booking.getBookingPurpose();
        room = booking.getRoom().getRoomName();
        id = booking.getId();
        periodicBookingId = booking.getPeriodicBookingId();
        start_time = booking.getStartTime();
        end_time= booking.getEndTime();
        employeeName = booking.getUser().getFullName();
    }
}
