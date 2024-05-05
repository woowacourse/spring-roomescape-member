package roomescape.time.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ReservationTime {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime of(final Long id, final String startAt) {
        validateTimeIsNotNull(startAt);
        return new ReservationTime(id, LocalTime.parse(startAt, TIME_FORMAT));
    }

    private static void validateTimeIsNotNull(final String time) {
        if (Objects.isNull(time)) {
            throw new NullPointerException("시간인 null인 경우 저장을 할 수 없습니다.");
        }
    }

    public ReservationTime(final Long id, final ReservationTime reservationTime) {
        this(id, reservationTime.startAt);
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
