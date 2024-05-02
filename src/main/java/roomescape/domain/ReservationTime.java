package roomescape.domain;

import roomescape.domain.exception.InvalidRequestException;
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

    public ReservationTime(final Long id, final String startAt) {
        this.id = id;
        this.startAt = LocalTime.parse(startAt);
    }

    public static ReservationTime from(final String startAt) {
        validateNull(startAt);
        validateFormat(startAt);
        return new ReservationTime(null, LocalTime.parse(startAt));
    }

    private static void validateNull(final String startAt) {
        if (startAt == null || startAt.isBlank()) {
            throw new InvalidRequestException("공백일 수 없습니다.");
        }
    }

    private static void validateFormat(final String startAt) {
        try {
            LocalTime.parse(startAt);
        } catch (DateTimeException e) {
            throw new InvalidTimeException("유효하지 않은 시간 입니다.");
        }
    }

    public ReservationTime assignId(final Long id) {
        return new ReservationTime(id, startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object obj) {
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
