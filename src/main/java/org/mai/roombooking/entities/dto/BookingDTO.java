package org.mai.roombooking.entities.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDTO implements Comparable<BookingDTO> {
    private Long bookingId;
    private String roomName;
    private String userFullName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String bookingPurpose;
    private Boolean isRecurrentBooking;

    @Override
    public int compareTo(BookingDTO other) {
        if (other == null) {
            return 1; // Любое ненулевое значение, если объект для сравнения null
        }

        if (this.startTime == null && other.startTime == null) {
            return 0; // Если оба startTime null, считаем их равными
        }

        if (this.startTime == null) {
            return -1; // Если только у текущего объекта startTime null, то он "меньше"
        }

        if (other.startTime == null) {
            return 1; // Если только у объекта для сравнения startTime null, то текущий объект "больше"
        }

        return this.startTime.compareTo(other.startTime);
    }
}
