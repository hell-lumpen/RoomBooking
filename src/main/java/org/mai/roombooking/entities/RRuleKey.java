package org.mai.roombooking.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class RRuleKey implements Serializable {

    private RRule.Frequency frequency;
    private Integer interval;
    private LocalDateTime untilDate;

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

