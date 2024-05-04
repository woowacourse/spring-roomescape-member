package roomescape.domain;

import roomescape.domain.exception.InvalidTimeException;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(final String startAt) {
        this.id = null;
        this.startAt = validateFormatAndConvert(startAt);
    }

    private LocalTime validateFormatAndConvert(final String startAt) {
        try {
            return LocalTime.parse(startAt);
        } catch (DateTimeException e) {
            throw new InvalidTimeException("유효하지 않은 시간 입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }
        final ReservationTime that = (ReservationTime) target;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ReservationTime[" +
                "id=" + id + ", " +
                "startAt=" + startAt + ']';
    }
}
