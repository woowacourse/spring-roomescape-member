package roomescape.reservation.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private Long id;
    private LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public void validate(LocalTime startAt) {
        if(startAt == null){
            throw new IllegalArgumentException("시작 시간은 null을 허용하지 않습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime reservationTime = (ReservationTime) o;
        return Objects.equals(id, reservationTime.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReservationTime{" +
                "id=" + id +
                ", startAt=" + startAt +
                '}';
    }
}
