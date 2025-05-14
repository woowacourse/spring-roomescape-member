package roomescape.reservationtime.domain;

import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionCause;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(Long id, LocalTime startAt) {
        validateTime(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime load(final Long id, final LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }

    public static ReservationTime create(final LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    private static void validateTime(final LocalTime startAt) {
        if (startAt == null) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_RESERVATION_TIME);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
