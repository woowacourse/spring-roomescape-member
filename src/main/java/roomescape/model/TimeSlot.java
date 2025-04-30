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

    private TimeSlot(final Long id, final LocalTime startAt) {
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

    public static TimeSlot register(final Long id, final LocalTime startAt) {
        return new TimeSlot(id, startAt);
    }

    public static TimeSlot create(final LocalTime startAt) {
        return new TimeSlot(null, startAt);
    }

    private void validateNotNull(final LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("모든 값들이 존재해야 합니다.");
        }
    }
}
