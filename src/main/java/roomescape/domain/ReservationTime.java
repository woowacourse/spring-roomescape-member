package roomescape.domain;

import roomescape.exception.BadRequestException;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private static final int TIME_UNIT = 10;

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, ReservationTime time) {
        this.id = id;
        this.startAt = time.startAt;
    }

    public ReservationTime(Long id, LocalTime startAt) {
        validateTimeUnit(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validateTimeUnit(LocalTime time) {
        if (time.getMinute() % TIME_UNIT != 0) {
            throw new BadRequestException("예약 시간은 10분 단위입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAt);
    }
}
