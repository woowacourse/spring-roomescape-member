package roomescape.business.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;
import roomescape.business.model.vo.Id;
import roomescape.business.model.vo.StartTime;

import java.time.LocalTime;
import java.util.Objects;

@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationTime {

    private static final int MINUTE_INTERVAL = 30;

    private final Id id;
    private final StartTime startTime;

    public static ReservationTime create(final LocalTime startTime) {
        return new ReservationTime(Id.issue(), new StartTime(startTime));
    }

    public static ReservationTime restore(final String id, final LocalTime startTime) {
        return new ReservationTime(Id.create(id), new StartTime(startTime));
    }

    public LocalTime startInterval() {
        return startTime.minusMinutes(MINUTE_INTERVAL);
    }

    public LocalTime endInterval() {
        return startTime.plusMinutes(MINUTE_INTERVAL);
    }

    public String id() {
        return id.value();
    }

    public LocalTime startAt() {
        return startTime.value();
    }

    @Override
    public final boolean equals(final Object o) {
        if (!(o instanceof final ReservationTime that)) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
