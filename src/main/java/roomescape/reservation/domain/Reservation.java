package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.message.ExceptionMessage;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class Reservation {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(final Long id, final String name, final String date, final ReservationTime time, final Theme theme) {
        validateInvalidName(name);
        validateDateIsNotNull(date);
        this.id = id;
        this.name = name;
        this.date = LocalDate.parse(date, DATE_FORMAT);
        this.time = time;
        this.theme = theme;
    }

    public static Reservation createWithId(final Long id, final String name, final String date, final ReservationTime time, final Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    public static Reservation createWithId(final Long id, final Reservation reservation) {
        return new Reservation(id, reservation.name, reservation.date.toString(), reservation.time, reservation.theme);
    }

    public static Reservation createWithOutId(final String name, final String date, final ReservationTime time, final Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }

    private void validateInvalidName(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new RoomEscapeException(ExceptionMessage.INVALID_USER_NAME);
        }
    }

    private void validateDateIsNotNull(final String date) {
        if (Objects.isNull(date)) {
            throw new NullPointerException("날짜가 null인 경우 저장을 할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time);
    }
}
