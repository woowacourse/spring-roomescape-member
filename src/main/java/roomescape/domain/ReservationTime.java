package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this.id = null;
        this.startAt = Objects.requireNonNull(startAt, "인자 중 null 값이 존재합니다.");
    }

    public ReservationTime(Long id, LocalTime startAt) {
        String errorMessage = "인자 중 null 값이 존재합니다.";
        this.id = Objects.requireNonNull(id, errorMessage);
        this.startAt = Objects.requireNonNull(startAt, errorMessage);
    }

    public ReservationTime changeId(Long id) {
        return new ReservationTime(id, this.startAt);
    }

    public boolean isBefore(LocalTime currentTime) {
        return startAt.isBefore(currentTime);
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
        ReservationTime time = (ReservationTime) o;
        return Objects.equals(id, time.id) && Objects.equals(startAt, time.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }
}
