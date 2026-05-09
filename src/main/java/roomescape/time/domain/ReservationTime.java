package roomescape.time.domain;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;
    private final LocalTime endAt;

    public ReservationTime(String startAt, String endAt) {
        this(null, parse(startAt), parse(endAt));
    }

    public ReservationTime(Long id, String startAt, String endAt) {
        this(id, parse(startAt), parse(endAt));
    }

    public ReservationTime(LocalTime startAt, LocalTime endAt) {
        this(null, startAt, endAt);
    }

    public ReservationTime(Long id, LocalTime startAt, LocalTime endAt) {
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
}
