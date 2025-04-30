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
        if (name.length() > MAX_NAME_LENGTH) {
            throw new OverMaxNameLengthException();
        }
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new NameContainsNumberException();
            }
        }
        if (time == null) {
            throw new ReservationTimeNotFoundException();
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation beforeSave(
            final String name,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        if (date.isBefore(LocalDate.now())) {
            throw new PastDateException();
        }
        long minusDays = Period.between(date, LocalDate.now()).getDays();
        if (minusDays > RESERVATION_START_INTERVAL) {
            throw new ReservationBeforeStartException();
        }
        return new Reservation(null, name, date, time, theme);
    }

    public static Reservation afterSave(
            final Long id,
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
