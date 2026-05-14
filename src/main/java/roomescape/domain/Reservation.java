package roomescape.domain;

import common.exception.ErrorCode;
import common.exception.RoomEscapeException;
import java.time.LocalDateTime;

public class Reservation {
    private final long id;
    private final Name name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(long id, Name name, ReservationDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation of(long id, Name name, ReservationDate date, ReservationTime time, Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    public static Reservation of(Name name, ReservationDate date, ReservationTime time, Theme theme) {
        return new Reservation(0L, name, date, time, theme);
    }

    public static Reservation reserve(Name name, ReservationDate date, ReservationTime time, Theme theme,
                                      LocalDateTime now) {
        validateIsNotNull(now);
        validateAvailableDateTime(date, time, now);
        return new Reservation(0L, name, date, time, theme);
    }

    private static void validateIsNotNull(LocalDateTime now) {
        if (now == null) {
            throw new IllegalStateException("현재 날짜를 입력해야 합니다.");
        }
    }

    private static void validateAvailableDateTime(ReservationDate requestDate, ReservationTime requestTime,
                                                  LocalDateTime now) {
        if (requestDate.isBefore(now.toLocalDate())) {
            throw new RoomEscapeException(ErrorCode.PAST_RESERVATION_NOT_ALLOWED);
        }

        if (requestDate.isEqual(now.toLocalDate()) && requestTime.isBefore(now.toLocalTime())) {
            throw new RoomEscapeException(ErrorCode.PAST_RESERVATION_NOT_ALLOWED);
        }
    }

    public long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
