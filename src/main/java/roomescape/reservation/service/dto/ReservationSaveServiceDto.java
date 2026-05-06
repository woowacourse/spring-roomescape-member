package roomescape.reservation.service.dto;

import java.time.LocalDate;

public class ReservationSaveServiceDto {
    private final String name;
    private final LocalDate date;
    private final Long themeId;
    private final String time;

    public ReservationSaveServiceDto(String name, LocalDate date, Long themeId, String time) {
        this.name = name;
        this.date = date;
        this.themeId = themeId;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }

    public String getTime() {
        return time;
    }
}
