package roomescape.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import roomescape.exception.InvalidReservationException;

public class ReservationTime {
    private final static String TIME_FORMAT = "HH:mm";
    private final static long NO_ID = 0;

    private final long id;
    private final LocalTime startAt;

    public ReservationTime(final long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(final long id, final ReservationTime reservationTime) {
        this(id, reservationTime.startAt);
    }

    public ReservationTime(final long id, final String time) {
        validate(time);
        this.id = id;
        this.startAt = LocalTime.parse(time);
    }

    public ReservationTime(final String time) {
        validate(time);
        this.id = NO_ID;
        this.startAt = LocalTime.parse(time);
    }

    private void validate(final String time) {
        try {
            LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            throw new InvalidReservationException("올바르지 않은 시간입니다.");
        }
    }

    public long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }
}
