package org.mai.roombooking.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Column(name = "booking_group_id")
    private UUID bookingGroupId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    List<User> staff;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Group> groups;

    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "booking_tag",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recurring_rule")
    private RecurringRule recurringRule;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        REQUIRES_CONFIRMATION,
        CONFIRMED,
        REJECTED

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) && Objects.equals(bookingGroupId, booking.bookingGroupId) &&
                Objects.equals(room, booking.room) && Objects.equals(owner, booking.owner) &&
                Objects.equals(staff, booking.staff) && Objects.equals(groups, booking.groups) &&
                Objects.equals(startTime, booking.startTime) && Objects.equals(endTime, booking.endTime)
                && Objects.equals(title, booking.title) && Objects.equals(description, booking.description)
                && Objects.equals(tags, booking.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookingGroupId, room, owner, staff, groups, startTime, endTime, title, description, tags);
    }

    @Override
    public String toString() {
        return  "bookingGroupId=" + bookingGroupId +
                "|| room=" + room.getRoomName() +
                "|| owner=" + owner.getFullName() +
                "|| staff=" + staff.stream().map(User::getFullName).collect(Collectors.joining(", ")) +
                "|| groups=" + groups.stream().map(Group::getName).collect(Collectors.joining(", ")) +
                "|| startTime=" + startTime +
                "|| endTime=" + endTime +
                "|| title='" + title + '\'' +
                "|| description='" + description + '\'' +
                "|| tags=" + tags.stream().map(Tag::getFullName).collect(Collectors.joining(", "));
    }
}