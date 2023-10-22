package org.mai.roombooking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.mai.roombooking.dtos.RoomBookingRequestDTO;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "periodic_booking_id")
    private Long periodicBookingId;

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

    @Column(name = "booking_purpose")
    private String bookingPurpose;

    @ManyToOne(fetch = FetchType.LAZY)
    private RRule rRule;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = JsonFormat.Shape.STRING)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
}