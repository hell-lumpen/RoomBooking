package org.mai.roombooking.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString()
@Table(name = "recurringrule")
public class RecurringRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "interval")
    private Integer interval;

    @Column(name = "unit")
    private String unit;

    @Column(name = "count")
    private Integer count;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "rule_exception",
            joinColumns = @JoinColumn(name = "rule_id"),
            inverseJoinColumns = @JoinColumn(name = "exception_id")
    )
    private Set<RecurringException> exceptions;


//    @OneToMany(fetch = FetchType.EAGER)
//    @Column(name = "exception_id")
//    private List<RecurringException> exceptions;

//    @ManyToOne
//    @JoinColumn(name = "booking_id")
//    private Booking bookings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecurringRule rrule = (RecurringRule) o;
        return Objects.equals(id, rrule.id) && Objects.equals(interval, rrule.interval) &&
                Objects.equals(unit, rrule.unit) && Objects.equals(count, rrule.count)
//                && Objects.equals(endTime, rrule.endTime) && Objects.equals(exceptions, rrule.exceptions)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, interval, unit, count, endTime
//                , exceptions
        );
    }

}
