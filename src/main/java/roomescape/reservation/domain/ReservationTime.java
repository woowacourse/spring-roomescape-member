package roomescape.reservation.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
        validate();
    }

    private void validate() {
        Objects.requireNonNull(startAt, "시작 시간은 null일 수 없습니다.");
    }

    public ReservationTime withId(Long id) {
        return new ReservationTime(id, this.startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public boolean isSameId(Long id) {
        return this.id.equals(id);
    }

}
