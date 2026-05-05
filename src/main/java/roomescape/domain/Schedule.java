package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private Long id;
    private final Long themeId;
    private final LocalDate date;
    private final LocalTime startAt;
    private Boolean isAvailable;

    public Schedule(Long id, Long themeId, LocalDate date, LocalTime startAt, Boolean isAvailable) {
        this.id = id;
        this.themeId = themeId;
        this.date = date;
        this.startAt = startAt;
        this.isAvailable = isAvailable;
    }

    public Long getId() {
        return id;
    }

    public Long getThemeId() {
        return themeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }
}
