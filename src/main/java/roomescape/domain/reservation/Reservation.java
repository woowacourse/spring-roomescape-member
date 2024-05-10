package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.domain.member.Member;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

public class Reservation {

    private final Long id;
    private final ReservationDate reservationDate;
    private final Member member;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id, ReservationDate reservationDate, Member member, ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.reservationDate = reservationDate;
        this.member = member;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(Long id, Reservation reservation) {
        this(id, reservation.reservationDate, reservation.member, reservation.reservationTime, reservation.theme);
    }

    public boolean isSameDate(LocalDate date) {
        return reservationDate.isSameDate(date);
    }

    public boolean isBeforeDate(LocalDate date) {
        return reservationDate.isBeforeDate(date);
    }

    public boolean isBeforeTime(LocalTime time) {
        return !reservationTime.isAfterOrSameTime(time);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public ReservationDate getDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
