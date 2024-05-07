package roomescape.time.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ReservationTime {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(final Long id, final String startAt) {
        validateTimeIsNotNull(startAt);
        this.id = id;
        this.startAt = LocalTime.parse(startAt, TIME_FORMAT);
    }

    public static ReservationTime createWithOutId(final String startAt) {
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime createWithId(final Long id, final ReservationTime reservationTime) {
        return new ReservationTime(id, reservationTime.startAt.toString());
    }

    public static ReservationTime createWithId(final Long id, final String startAt) {
        return new ReservationTime(id, startAt);
    }

    private void validateTimeIsNotNull(final String time) {
        if (Objects.isNull(time)) {
            throw new NullPointerException("시간인 null인 경우 저장을 할 수 없습니다.");
        }
    }

    public boolean checkPastTime() {
        return startAt.isBefore(LocalTime.now());
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
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id) && Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }
}
