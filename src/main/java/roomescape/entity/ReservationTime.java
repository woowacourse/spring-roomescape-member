package roomescape.entity;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    public static final LocalTime START_RESERVATION_TIME = LocalTime.of(10, 0);
    public static final LocalTime LAST_RESERVATION_TIME = LocalTime.of(23, 0);

    private Long id;
    private LocalTime startAt;

    public ReservationTime() {
    }

    public ReservationTime(final LocalTime startAt) {
        validateTime(startAt);
        this.startAt = startAt;
    }

    public ReservationTime(final Long id) {
        this.id = id;
    }

    public ReservationTime(final long id, final LocalTime startAt) {
        this(startAt);
        this.id = id;
    }

    private void validateTime(final LocalTime time) {
        if (time.isBefore(START_RESERVATION_TIME) || time.isAfter(LAST_RESERVATION_TIME)) {
            throw new IllegalArgumentException("예약할 수 없는 시간입니다.");
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public boolean isInTimeInterval(LocalTime otherTime) {
        long minuteDifference = Duration.between(startAt, otherTime).abs().toMinutes();
        return minuteDifference < 30;
    }
}
