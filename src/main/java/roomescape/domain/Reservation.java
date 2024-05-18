package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import roomescape.domain.policy.CurrentDueTimePolicy;
import roomescape.domain.policy.ReservationDueTimePolicy;
import roomescape.exception.reservation.InvalidDateTimeReservationException;


public class Reservation {
    private final Long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final Member member;

    public Reservation(Long id, LocalDate date, ReservationTime time, Theme theme, Member member) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(LocalDate date, ReservationTime time, Theme theme, Member member) {
        this(null, date, time, theme, member);
        validateDateTimeReservation(date.atTime(time.getStartAt()), new CurrentDueTimePolicy());
    }

    public Reservation(LocalDate date, ReservationTime time, Theme theme, Member member,
                       ReservationDueTimePolicy dueTimePolicy) {
        this(null, date, time, theme, member);
        validateDateTimeReservation(date.atTime(time.getStartAt()), dueTimePolicy);
    }

    private void validateDateTimeReservation(LocalDateTime dateTime, ReservationDueTimePolicy timePolicy) {
        if (dateTime.isBefore(timePolicy.getDueTime())) {
            throw new InvalidDateTimeReservationException();
        }
    }

    public Long getId() {
        return id;
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

    public Member getMember() {
        return member;
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
