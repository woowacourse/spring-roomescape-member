package roomescape.time.domain;

import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"id"})
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        validateNotNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(final LocalTime startAt) {
        this(null, startAt);
    }

    private void validateNotNull(final LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("time cannot be null");
        }
    }
}
