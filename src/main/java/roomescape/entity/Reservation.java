package roomescape.entity;

import roomescape.exception.impl.*;

import java.time.LocalDate;
import java.time.Period;

public class Reservation {

    private static final int MAX_NAME_LENGTH = 10;
    private static final int RESERVATION_START_INTERVAL = 7;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(
            final Long id,
            final String name,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        validateMaxNameLength(name);
        validateNameDoesNotContainsNumber(name);
        validateTimeIsNull(time);
        validateThemeIsNull(theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateMaxNameLength(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new OverMaxNameLengthException();
        }
    }

    private void validateNameDoesNotContainsNumber(final String name) {
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new NameContainsNumberException();
            }
        }
    }

    private void validateTimeIsNull(final ReservationTime time) {
        if (time == null) {
            throw new ReservationTimeNotFoundException();
        }
    }

    private void validateThemeIsNull(final Theme theme) {
        if (theme == null) {
            throw new ReservationThemeNotFoundException();
        }
    }

    public static Reservation beforeSave(
            final String name,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        validateDateIsNotPast(date);
        validateDateInterval(date);
        return new Reservation(null, name, date, time, theme);
    }

    private static void validateDateInterval(final LocalDate date) {
        long minusDays = Period.between(LocalDate.now(), date).getDays();
        System.out.println(minusDays);
        if (minusDays > RESERVATION_START_INTERVAL) {
            throw new ReservationBeforeOpenDayException();
        }
    }

    private static void validateDateIsNotPast(final LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new PastDateException();
        }
    }

    public static Reservation afterSave(
            final long id,
            final String name,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        return new Reservation(id, name, date, time, theme);
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
}
