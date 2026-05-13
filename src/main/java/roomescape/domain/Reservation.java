package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private final Long id;
    private final String name;
    private final Theme theme;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, String name, Theme theme, LocalDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public boolean isInPast(LocalDateTime currentDateTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        return reservationDateTime.isBefore(currentDateTime);
    }

    public Reservation withId(Long id) {
        return new Reservation(id, name, theme, date, time);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
