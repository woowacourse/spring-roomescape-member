package roomescape.time.domain;

import roomescape.exception.BadRequestException;

import java.time.LocalTime;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateNullField(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validateNullField(LocalTime startAt) {
        if (startAt == null) {
            throw new BadRequestException("예약 시간 필드에는 빈 값이 들어올 수 없습니다.");
        }
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public boolean isDuplicated(ReservationTime other) {
        return startAt.equals(other.startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
