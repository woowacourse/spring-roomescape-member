package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final TimeSlot time;
    private final Theme theme;

    public Reservation(final Long id, final String name, final LocalDate date, final TimeSlot time, final Theme theme) {
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
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public TimeSlot getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
