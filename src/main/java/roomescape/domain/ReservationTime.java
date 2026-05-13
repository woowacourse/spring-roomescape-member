package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public record ReservationTime(Long id, LocalTime startAt) {
    public boolean isBefore(LocalTime time) {
        return startAt.isBefore(time);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ReservationTime that)) return false;
        return Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(startAt);
    }
}
