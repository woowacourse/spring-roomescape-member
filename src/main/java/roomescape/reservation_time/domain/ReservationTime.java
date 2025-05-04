package roomescape.reservation_time.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateId(id);
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime time) {
        validateStartAt(time);
        this.id = null;
        this.startAt = time;
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("예약 ID가 없습니다.");
        }
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("예약 시작 시간이 없습니다.");
        }
    }

    public boolean isSameTime(ReservationTime reservationTime) {
        return this.startAt.equals(reservationTime.startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ReservationTime that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
