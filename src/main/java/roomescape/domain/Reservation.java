package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final LocalDate date;
    private final Member member;
    private final ReservationTime time;
    private final RoomTheme theme;

    public Reservation(LocalDate date, Member member, ReservationTime time, RoomTheme theme) {
        this(null, date, member, time, theme);
    }

    public Reservation(Long id, Reservation reservation) {
        this(id, reservation.date, reservation.member, reservation.time, reservation.theme);
    }

    public Reservation(Long id, LocalDate date, Member member, ReservationTime time,
                       RoomTheme theme) {
        this.id = id;
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Member getMember() {
        return member;
    }

    public ReservationTime getTime() {
        return time;
    }

    public RoomTheme getTheme() {
        return theme;
    }
}
