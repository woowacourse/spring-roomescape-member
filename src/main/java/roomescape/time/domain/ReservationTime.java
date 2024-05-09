package roomescape.time.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import roomescape.exception.RoomEscapeException;

public class ReservationTime {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(final Long id, final String startAt) {
        this.id = id;
        this.startAt = parseTime(startAt);
    }

    public ReservationTime(final String startAt) {
        this(null, startAt);
    }

    public ReservationTime(final Long id, final ReservationTime reservationTime) {
        this(id, reservationTime.startAt.toString());
    }

    private LocalTime parseTime(final String time) {
        try {
            return LocalTime.parse(time, TIME_FORMAT);
        } catch (NullPointerException | DateTimeParseException e) {
            throw new RoomEscapeException("형식에 맞지 않은 시간입니다.");
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
