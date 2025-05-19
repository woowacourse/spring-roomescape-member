package roomescape.business;

import java.time.LocalDate;

public final class Reservation {

    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final ReservationTheme theme;

    public Reservation(Long id, Member member, LocalDate date, ReservationTime time, ReservationTheme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Member member, LocalDate date, ReservationTime time, ReservationTheme theme) {
        this(null, member, date, time, theme);
    }

    public boolean isSameReservation(Reservation otherReservation) {
        return this.date.equals(otherReservation.date)
                && this.time.getId().equals(otherReservation.time.getId())
                && this.theme.getId().equals(otherReservation.theme.getId());
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

    public ReservationTheme getTheme() {
        return theme;
    }
}
