package roomescape.business.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import roomescape.business.model.vo.Id;
import roomescape.exception.business.InvalidCreateArgumentException;

import java.time.LocalTime;

@ToString
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationTime {

    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    private static final LocalTime END_TIME = LocalTime.of(23, 0);
    private static final int MINUTE_INTERVAL = 30;

    private final Id id;
    private final LocalTime startAt;

    public static ReservationTime create(final LocalTime startAt) {
        validateTimeAvailable(startAt);
        return new ReservationTime(Id.issue(), startAt);
    }

    public static ReservationTime restore(final String id, final LocalTime startAt) {
        validateTimeAvailable(startAt);
        return new ReservationTime(Id.create(id), startAt);
    }

    private static void validateTimeAvailable(final LocalTime time) {
        if (time.isBefore(START_TIME) || time.isAfter(END_TIME)) {
            throw new InvalidCreateArgumentException("예약은 10시~23시로만 가능합니다.");
        }
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
