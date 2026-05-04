package roomescape.dto;

import java.time.LocalDate;

public class ReservationRequest {
    private final String name;
    private final LocalDate date;
    private final Long timeId;
    private final Long themeId;

    public ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }
}
