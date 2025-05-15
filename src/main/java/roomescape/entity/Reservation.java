package roomescape.entity;

import roomescape.exception.impl.PastDateException;
import roomescape.exception.impl.ReservationBeforeOpenDayException;
import roomescape.exception.impl.ReservationThemeNotFoundException;
import roomescape.exception.impl.ReservationTimeNotFoundException;

import java.time.LocalDate;
import java.time.Period;

public class Reservation {

    private static final int RESERVATION_START_INTERVAL = 7;

    private final Long id;
    private final LocalDate date;
    private final Member member;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(
            final Long id,
            final Member member,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        validateTimeIsNull(time);
        validateThemeIsNull(theme);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
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
            final LocalDate date,
            final Member member,
            final ReservationTime time,
            final Theme theme
    ) {
        validateDateIsNotPast(date);
        validateDateInterval(date);
        return new Reservation(null, member, date, time, theme);
    }

    private static void validateDateInterval(final LocalDate date) {
        long minusDays = Period.between(LocalDate.now(), date).getDays();
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
            final LocalDate date,
            final Member member,
            final ReservationTime time,
            final Theme theme
    ) {
        return new Reservation(id, member, date, time, theme);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
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
