package roomescape.business.model.entity;

import roomescape.business.model.vo.Id;
import roomescape.exception.business.InvalidReservationTimeException;

import java.time.LocalTime;

public class ReservationTime {

    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    private static final LocalTime END_TIME = LocalTime.of(23, 0);
    private static final int MINUTE_INTERVAL = 30;

    private final Id id;
    private final LocalTime startAt;

    private ReservationTime(final Id id, final LocalTime startAt) {
        validateTimeAvailable(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validateTimeAvailable(final LocalTime time) {
        if (time.isBefore(START_TIME) || time.isAfter(END_TIME)) {
            throw new InvalidReservationTimeException();
        }
    }

    public static ReservationTime create(final LocalTime startAt) {
        return new ReservationTime(Id.issue(), startAt);
    }

    public static ReservationTime restore(final String id, final LocalTime startAt) {
        return new ReservationTime(Id.create(id), startAt);
    }

    public LocalTime startInterval() {
        return startAt.minusMinutes(MINUTE_INTERVAL);
    }

    public LocalTime endInterval() {
        return startAt.plusMinutes(MINUTE_INTERVAL);
    }

    public String id() {
        return id.value();
    }

    public LocalTime startAt() {
        return startAt;
    }
}
