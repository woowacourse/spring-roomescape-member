package roomescape.domain;

import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationTime {

    @EqualsAndHashCode.Include
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(final LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(final String input) {
        this(null, LocalTime.parse(input));
    }

    public ReservationTime assignId(final Long id) {
        return new ReservationTime(id, startAt);
    }

    public boolean isSameTime(final ReservationTime other) {
        return this.startAt.equals(other.startAt);
    }
}
