package org.mai.roombooking.entities;

import java.time.LocalDateTime;

public class RRule {
    private Frequency frequency;
    private Integer interval;
    private Integer count;
    private LocalDateTime untilDate;

    public enum Frequency {
        DAILY, WEEKLY, MONTHLY, YEARLY
    }
}