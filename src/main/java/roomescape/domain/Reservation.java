package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private Name name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    public Reservation() {
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.name = new Name(name);
        this.date = date;
        this.time = reservationTime;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this(null, name, date, reservationTime, theme);
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

    public Theme getTheme() {
        return theme;
    }
}
