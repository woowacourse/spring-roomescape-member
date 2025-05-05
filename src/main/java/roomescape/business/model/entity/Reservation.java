package roomescape.business.model.entity;

import roomescape.business.model.vo.Id;
import roomescape.exception.impl.PastDateException;
import roomescape.exception.impl.ReservationBeforeStartException;
import roomescape.exception.impl.ReservationTimeNotFoundException;

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

    public static Reservation beforeSave(
            final User user,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        validateDateIsNotPast(date);
        validateDateInterval(date);
        return new Reservation(Id.nullId(), user, date, time, theme);
    }

    private static void validateDateInterval(final LocalDate date) {
        long minusDays = Period.between(LocalDate.now(), date).getDays();
        if (minusDays > DAY_INTERVAL_FROM_NOW) {
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
            final User user,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        return new Reservation(Id.create(id), user, date, time, theme);
    }

    public Long getId() {
        return id.longValue();
    }

    public String getName() {
        return user.name();
    }

    public String getUserEmail() {
        return user.email();
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

    public User getUser() {
        return user;
    }
}
