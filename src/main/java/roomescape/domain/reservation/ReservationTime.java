package roomescape.domain.reservation;

import java.time.LocalTime;
import roomescape.common.exception.BusinessRuleViolationException;

public record ReservationTime(Long id, LocalTime startAt) {

    public static final LocalTime RESERVATION_START_TIME = LocalTime.of(12, 0);
    public static final LocalTime RESERVATION_END_TIME = LocalTime.of(22, 0);

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime {
        if (startAt.isBefore(RESERVATION_START_TIME) || startAt.isAfter(RESERVATION_END_TIME)) {
            throw new BusinessRuleViolationException("해당 시간은 예약 가능 시간이 아닙니다.");
        }
    }
}
