package org.mai.roombooking.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@IdClass(RRuleKey.class)
public class RRule {
    @Id
    @Column(nullable = false)
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