package roomescape.domain.reservation.entity;

import java.time.LocalTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import roomescape.common.exception.InvalidArgumentException;

@Getter
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    @Builder
    public ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
        validateReservationTime();
    }

    public static ReservationTime withoutId(final LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    private void validateReservationTime() {
        if (startAt == null) {
            throw new InvalidArgumentException("startAt cannot be null");
        }
    }

    public boolean existId() {
        return id != null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id);
    }
}
