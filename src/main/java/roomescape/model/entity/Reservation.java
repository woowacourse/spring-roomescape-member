package roomescape.model.entity;

import roomescape.exception.impl.*;
import roomescape.model.vo.Id;

import java.time.LocalDate;
import java.time.Period;

public class Reservation {

    private static final int MAX_NAME_LENGTH = 10;
    private static final int RESERVATION_START_INTERVAL = 7;

    private final Id id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(
            final Id id,
            final String name,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        validateMaxNameLength(name);
        validateNameDoesNotContainsNumber(name);
        if (time == null) {
            throw new ReservationTimeNotFoundException();
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateNameDoesNotContainsNumber(final String name) {
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new NameContainsNumberException();
            }
        }
    }

    private void validateMaxNameLength(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new OverMaxNameLengthException();
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
        return new Reservation(Id.nullId(), name, date, time, theme);
    }

    private static void validateDateInterval(final LocalDate date) {
        long minusDays = Period.between(date, LocalDate.now()).getDays();
        if (minusDays > RESERVATION_START_INTERVAL) {
            throw new ReservationBeforeStartException();
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
        return new Reservation(Id.create(id), name, date, time, theme);
    }

    public Long getId() {
        return id.longValue();
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
