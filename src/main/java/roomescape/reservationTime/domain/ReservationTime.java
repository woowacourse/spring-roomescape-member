package roomescape.reservationTime.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private static final String NULL_VALUE_EXCEPTION_MESSAGE = "널 값은 저장될 수 없습니다.";

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this.id = null;
        this.startAt = Objects.requireNonNull(startAt, NULL_VALUE_EXCEPTION_MESSAGE);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = Objects.requireNonNull(id, NULL_VALUE_EXCEPTION_MESSAGE);
        this.startAt = Objects.requireNonNull(startAt, NULL_VALUE_EXCEPTION_MESSAGE);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) other;
        return Objects.equals(id, that.id) && Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }
}
