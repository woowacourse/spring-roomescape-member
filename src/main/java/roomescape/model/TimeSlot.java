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

    public TimeSlot(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public TimeSlot(final LocalTime startAt) {
        this(null, startAt);
    }

    public boolean isSameTimeSlot(final TimeSlot timeSlot) {
        return this.id.equals(timeSlot.id());
    }
}
