package roomescape.model;

import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(of = {"id"})
@Getter
@Accessors(fluent = true)
@ToString
public class TimeSlot {

    private final Long id;
    private final LocalTime startAt;

    public TimeSlot(final LocalTime startAt) {
        this(null, startAt);
    }

    public TimeSlot(final Long id, final LocalTime startAt) {
        validateNotNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public boolean isBefore(final LocalTime time) {
        return this.startAt.isBefore(time);
    }

    public boolean isSameTimeSlot(final TimeSlot timeSlot) {
        return this.id.equals(timeSlot.id());
    }

    private void validateNotNull(final LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("모든 값들이 존재해야 합니다.");
        }
    }
}
