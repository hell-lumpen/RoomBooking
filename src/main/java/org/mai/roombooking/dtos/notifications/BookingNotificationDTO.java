package org.mai.roombooking.dtos.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;

public record BookingNotificationDTO(Action action, RoomBookingDTO booking) {
    public enum Action {UPDATE, DELETE, CREATE}
}
