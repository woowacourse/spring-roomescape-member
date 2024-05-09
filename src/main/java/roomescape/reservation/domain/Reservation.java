package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.regex.Pattern;
import roomescape.exception.model.RoomEscapeException;
import roomescape.reservation.exception.ReservationExceptionCode;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

public class Reservation {

    private static final Pattern ILLEGAL_NAME_REGEX = Pattern.compile(".*[^\\w\\s가-힣].*");

    private long id;
    private final String name;
    private final LocalDate date;
    private Time time;
    private Theme theme;

    private Reservation(long id, String name, LocalDate date, Time time, Theme theme) {
        validate(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private Reservation(String name, LocalDate date, long timeId, long themeId) {
        this(0, name, date, new Time(timeId), new Theme(themeId));
    }

    public static Reservation reservationOf(long id, String name, LocalDate date, Time time, Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    public static Reservation saveReservationOf(String name, LocalDate date, long timeId, long themeId) {
        validateAtSave(date);
        return new Reservation(name, date, timeId, themeId);
    }

    public static Reservation saveReservationOf(String name, LocalDate date, Time time, Theme theme) {
        validateAtSaveDateAndTime(date, time);
        return new Reservation(0, name, date, time, theme);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getReservationTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setIdOnSave(long id) {
        this.id = id;
    }

    public boolean hasSameId(long id) {
        return this.id == id;
    }

    private static void validateTime(Time time) {
        if (time.getStartAt().isBefore(LocalTime.now())) {
            throw new RoomEscapeException(ReservationExceptionCode.RESERVATION_TIME_IS_PAST_EXCEPTION);
        }
    }

    private static void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new RoomEscapeException(ReservationExceptionCode.NAME_IS_NULL_OR_BLANK_EXCEPTION);
        }
        if (ILLEGAL_NAME_REGEX.matcher(name)
                .matches()) {
            throw new RoomEscapeException(ReservationExceptionCode.ILLEGAL_NAME_FORM_EXCEPTION);
        }
    }

    private static void validateAtSave(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new RoomEscapeException(ReservationExceptionCode.RESERVATION_DATE_IS_PAST_EXCEPTION);
        }
    }

    private static void validateAtSaveDateAndTime(LocalDate date, Time time) {
        validateAtSave(date);

        if (date.equals(LocalDate.now())) {
            validateTime(time);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
