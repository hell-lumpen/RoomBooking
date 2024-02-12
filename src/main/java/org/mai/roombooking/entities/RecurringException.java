package org.mai.roombooking.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString()
@Table(name = "recurringexception")
public class RecurringException {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecurringException recurringException = (RecurringException) o;
        return Objects.equals(id, recurringException.id) && Objects.equals(date, recurringException.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }

}
