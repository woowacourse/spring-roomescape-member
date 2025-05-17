package roomescape.time.domain;

import java.time.LocalTime;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시작 시간은 null일 수 없습니다.");
        }
    }

    public ReservationTime createWithId(Long id) {
        return new ReservationTime(id, this.startAt);
    }

    public boolean isPastTime(LocalTime nowTime) {
        return this.startAt.isBefore(nowTime);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

}
