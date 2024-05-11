package roomescape.domain;

import roomescape.domain.exception.InvalidTimeException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id) {
        this.id = id;
        this.startAt = null;
    }

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(Long id, String startAt) {
        validateTimeBlank(startAt);
        validateTimeFormat(startAt);

        this.id = id;
        this.startAt = LocalTime.parse(startAt);
    }

    private void validateTimeBlank(String startAt) {
        if (startAt == null || startAt.isBlank()) {
            throw new InvalidTimeException("시간은 공백일 수 없습니다.");
        }
    }

    private void validateTimeFormat(String startAt) {
        try {
            LocalTime.parse(startAt);
        } catch (DateTimeParseException e) {
            throw new InvalidTimeException("시간 형식이 올바르지 않습니다.");
        }
    }

    public ReservationTime assignId(Long id) {
        return new ReservationTime(id, startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ReservationTime) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }

    @Override
    public String toString() {
        return "ReservationTime[" +
                "id=" + id + ", " +
                "startAt=" + startAt + ']';
    }
}
