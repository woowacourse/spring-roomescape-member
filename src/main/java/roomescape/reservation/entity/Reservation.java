package roomescape.reservation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private final Long id;
    private String name;
    private LocalDate date;
    private Time time;
    private Long themeId;

    public Reservation(Long id, String name, LocalDate date, Time time, Long themeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
    }

    public boolean isDuplicatedWith(Reservation other) {
        return date.isEqual(other.date) && time.isDuplicatedWith(other.time);
    }

    public String getFormattedDate() {
        return date.toString();
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }

    public Long getTimeId() {
        return time.getId();
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
