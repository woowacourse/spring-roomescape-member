package roomescape.reservation.domain;

import roomescape.member.domain.MemberName;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private static final long NO_ID = 0;

    private final long id;
    private final MemberName name;
    private final Schedule schedule;
    private final Theme theme;

    public Reservation(long id, MemberName name, Schedule schedule, Theme theme) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.theme = theme;
    }

    public Reservation(long id, Reservation reservation) {
        this(id, reservation.name, reservation.schedule, reservation.theme);
    }

    public Reservation(long id, String name, String date, ReservationTime reservationTime,
                       final Theme theme) {
        this(id, new MemberName(name), new Schedule(new ReservationDate(date), reservationTime), theme);
    }

    public Reservation(String name, String reservationDate, ReservationTime reservationTime, Theme theme) {
        this(NO_ID, new MemberName(name), new Schedule(new ReservationDate(reservationDate), reservationTime), theme);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public LocalDate getDate() {
        return schedule.getDate();
    }

    public LocalTime getTime() {
        return schedule.getTime();
    }

    public ReservationTime getReservationTime() {
        return schedule.getReservationTime();
    }

    public Theme getTheme() {
        return theme;
    }
}
