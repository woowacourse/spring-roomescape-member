package roomescape.reservation;

import java.time.LocalDate;
import roomescape.time.Time;

public class Reservation {
    private Long id;
    private final String name;
    private final LocalDate date;
    private final Time time;
    private final Long themeId;

    public Reservation(String name, LocalDate date, Time time, Long themeId) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
    }

    public Reservation(Long id, String name, LocalDate date, Time time, Long themeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
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

    public Time getTime() {
        return time;
    }

    public Long getThemeId() {
        return themeId;
    }
}
