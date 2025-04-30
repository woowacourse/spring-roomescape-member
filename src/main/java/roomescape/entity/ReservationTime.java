package roomescape.entity;

import roomescape.exception.impl.InvalidReservationTimeException;

import java.time.Duration;
import java.time.LocalTime;

public class ReservationTime {

    private static final LocalTime START_RESERVATION_TIME = LocalTime.of(10, 0);
    private static final LocalTime LAST_RESERVATION_TIME = LocalTime.of(23, 0);

    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(final Long id, final LocalTime startAt) {
        validateTime(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validateTime(final LocalTime time) {
        if (time.isBefore(START_RESERVATION_TIME) || time.isAfter(LAST_RESERVATION_TIME)) {
            throw new InvalidReservationTimeException();
        }
    }

    public static ReservationTime afterSave(final Long id, final LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }

    public static ReservationTime beforeSave(final LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public boolean isInTimeInterval(LocalTime otherTime) {
        long minuteDifference = Duration.between(startAt, otherTime).abs().toMinutes();
        return minuteDifference < 30;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
