package roomescape.reservation.domain;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Long themeId;

    public Reservation(String name, LocalDate date, ReservationTime time, Long themeId) {
        this(null, name, date, time, themeId);
    }

    private Reservation(Long id, String name, LocalDate date, ReservationTime time, Long themeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
    }

    public Reservation withId(Long id) {
        return new Reservation(id, this.name, this.date, this.time, this.themeId);
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

    public Long getThemeId() {
        return themeId;
    }
}
