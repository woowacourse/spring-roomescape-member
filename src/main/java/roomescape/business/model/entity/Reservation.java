package roomescape.business.model.entity;

import roomescape.business.model.vo.Id;
import roomescape.exception.business.PastDateException;
import roomescape.exception.business.ReservationBeforeStartException;
import roomescape.exception.business.ReservationTimeNotFoundException;

import java.time.LocalDate;
import java.time.Period;

public class Reservation {

    private static final int DAY_INTERVAL_FROM_NOW = 7;

    private final Id id;
    private final User user;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(
            final Id id,
            final User user,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        if (time == null) {
            throw new ReservationTimeNotFoundException();
        }
        this.id = id;
        this.user = user;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation create(final User user, final LocalDate date, final ReservationTime time, final Theme theme) {
        validateDateIsNotPast(date);
        validateDateInterval(date);
        return new Reservation(Id.issue(), user, date, time, theme);
    }

    private static void validateDateIsNotPast(final LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new PastDateException();
        }
    }

    private static void validateDateInterval(final LocalDate date) {
        long minusDays = Period.between(LocalDate.now(), date).getDays();
        if (minusDays > DAY_INTERVAL_FROM_NOW) {
            throw new ReservationBeforeStartException();
        }
    }

    public static Reservation restore(final String id, final User user, final LocalDate date, final ReservationTime time, final Theme theme) {
        return new Reservation(Id.create(id), user, date, time, theme);
    }

    public String id() {
        return id.value();
    }

    public String reserverName() {
        return user.name();
    }

    public LocalDate date() {
        return date;
    }

    public ReservationTime time() {
        return time;
    }

    public Theme theme() {
        return theme;
    }

    public User reserver() {
        return user;
    }
}
