package org.mai.roombooking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mai.roombooking.entities.dto.BookingDTO;
import org.mai.roombooking.entities.dto.BookingDetailsDTO;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "Bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

    @Column(name = "booking_purpose", length = 255)
    private String bookingPurpose;

    @Column(name = "recurring_interval")
    private Integer recurringInterval;

    @Column(name = "recurring_unit", length = 10)
    @Enumerated(EnumType.STRING)
    private RecurrentUnit recurringUnit;

    @Column(name = "recurring_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = JsonFormat.Shape.STRING)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime recurringEndTime;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = JsonFormat.Shape.STRING)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

//    Ограничения

//    @PrePersist
//    public void prePersist() {
//        // Условие проверки, что начальное время меньше конечного времени
//
//        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
//            throw new IllegalArgumentException("Start time must be before end time.");
//        }
//
//        // Условие проверки, что если есть интервал повторения, то указаны и единица, и количество
//        if ((recurringInterval != null || recurringUnit != null || recurringEndTime != null) &&
//                (recurringInterval == null || recurringUnit == null || recurringEndTime == null)) {
//            throw new IllegalArgumentException("If there is a recurring interval, both unit and count must be specified.");
//        }
//
//        this.createdAt = LocalDateTime.now();
//    }

    public BookingDTO toDTO() {
        return BookingDTO.builder()
                .bookingId(this.bookingId)
                .roomName(this.room.getRoomName())
                .userFullName(this.user.getFullName())
                .bookingPurpose(this.bookingPurpose)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .isRecurrentBooking(this.recurringUnit != null)
                .build();
    }

    public BookingDetailsDTO toDetailsDTO() {
        return BookingDetailsDTO.builder()
                .bookingId(this.bookingId)
                .room(this.room)
                .user(this.user)
                .bookingPurpose(this.bookingPurpose)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .recurringUnit(this.recurringUnit)
                .recurringInterval(this.recurringInterval)
                .recurrentEndTime(this.recurringEndTime)
                .build();
    }
}