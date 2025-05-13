package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;
    private final Member member;

    public Reservation(Long id, LocalDate date, ReservationTime reservationTime, Theme theme,
                       Member member) {
        this.member = member;
        this.id = id;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(LocalDate date, ReservationTime reservationTime, Theme theme, Member member) {
        this(null, date, reservationTime, theme, member);
    }

    public Reservation copyWithId(Long id) {
        return new Reservation(id, date, reservationTime, theme, member);
    }

    public void validatePastDateTime() {
        LocalDate now = LocalDate.now();
        if (date.isBefore(now) || (date.equals(now) && reservationTime.isPastTime())) {
            throw new IllegalArgumentException("지난 날짜와 시간의 예약은 생성 불가능합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }
}
