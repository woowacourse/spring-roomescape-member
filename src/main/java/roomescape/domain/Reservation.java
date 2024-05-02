package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    public Reservation() {
    }

    public Reservation(final String name, final LocalDate date, final ReservationTime time, final Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time, final Theme theme) {
        validateReservation(name, date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private static void validateReservation(final String name, final LocalDate date, final ReservationTime time) {
        if (name == null || name.isBlank() || date == null || time == null) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
