package org.mai.roombooking.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class RRuleKey implements Serializable {

    private RRule.Frequency frequency;
    private Integer interval;
    private LocalDateTime untilDate;

    public RRuleKey() {
    }

    public RRuleKey(RRule.Frequency frequency, Integer interval, LocalDateTime untilDate) {
        this.frequency = frequency;
        this.interval = interval;
        this.untilDate = untilDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RRuleKey rRuleKey = (RRuleKey) o;
        return frequency == rRuleKey.frequency &&
                Objects.equals(interval, rRuleKey.interval) &&
                Objects.equals(untilDate, rRuleKey.untilDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frequency, interval, untilDate);
    }
}

