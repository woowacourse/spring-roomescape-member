package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;
    private final RoomTheme theme;

    public Reservation(Name name, LocalDate date, ReservationTime time, RoomTheme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, Reservation reservation) {
        this(id, reservation.name, reservation.date, reservation.time, reservation.theme);
    }

    public Reservation(Long id, Name name, LocalDate date, ReservationTime time, RoomTheme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public RoomTheme getTheme() {
        return theme;
    }
}
