package org.mai.roombooking.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
@IdClass(RRuleKey.class)
public class RRule {
    @Id
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @Id
    @Column(nullable = false)
    private Integer interval;

    @Id
    @Column(nullable = false)
    private LocalDateTime untilDate;

    public enum Frequency {
        DAILY, WEEKLY, MONTHLY, YEARLY
    }
}