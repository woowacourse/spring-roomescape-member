package roomescape.time.domain;

import roomescape.exception.BusinessRuleViolationException;
import roomescape.exception.InvalidRequestException;

import java.time.LocalTime;

public class ReservationTime {

    private static final int UNIT = 30;

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateStartAtNotNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime fromValidTimeUnit(LocalTime startAt) {
        validateStartAtUnit(startAt);
        return new ReservationTime(null, startAt);
    }

    private static void validateStartAtUnit(LocalTime startAt) {
        if (startAt.getMinute() % UNIT != 0) {
            throw new BusinessRuleViolationException("예약은 30분 단위로 입력해야 합니다.");
        }
    }

    private void validateStartAtNotNull(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidRequestException("예약 시간은 반드시 입력해야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
