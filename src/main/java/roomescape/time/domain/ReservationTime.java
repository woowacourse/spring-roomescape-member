package roomescape.time.domain;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;
    private final LocalTime endAt;

    public ReservationTime(LocalTime startAt, LocalTime endAt) {
        this(null, startAt, endAt);
    }

    public ReservationTime(Long id, LocalTime startAt, LocalTime endAt) {
        validateTimeRange(startAt, endAt);
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public ReservationTime withId(Long id) {
        return new ReservationTime(id, startAt, endAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public LocalTime getEndAt() {
        return endAt;
    }

    public static LocalTime parse(String time) {
        try {
            return LocalTime.parse(time);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("예약 시간 형식이 올바르지 않습니다. time=" + time);
        }
    }

    private void validateTimeRange(LocalTime startAt, LocalTime endAt) {
        if (startAt == null || endAt == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
        if (!endAt.isAfter(startAt)) {
            throw new IllegalArgumentException("예약 종료 시간은 시작 시간보다 늦어야 합니다.");
        }
    }
}
