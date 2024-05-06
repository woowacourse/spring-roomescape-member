package roomescape.controller.request;

import roomescape.exception.BadRequestException;

import java.time.LocalDate;

public class ReservationRequest {

    private final String name;
    private final LocalDate date;
    private final long timeId;
    private final long themeId;

    public ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {
        validateNull(date, timeId, themeId);
        validateName(name);
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    private void validateNull(LocalDate date, Long timeId, Long themeId) {
        if (date == null || timeId == null || themeId == null) {
            throw new BadRequestException("[ERROR] 유효하지 않은 요청입니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("[ERROR] 유효하지 않은 요청입니다.");
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public long getTimeId() {
        return timeId;
    }

    public long getThemeId() {
        return themeId;
    }
}
