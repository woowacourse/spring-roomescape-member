package roomescape.controller.request;

import java.time.LocalDate;

import roomescape.exception.BadRequestException;

public class ReservationRequest {

    private final String name;
    private final LocalDate date;
    private final long timeId;
    private final long themeId;

    public ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {
        validateName(name, "name");
        validateId(timeId, "timeId");
        validateId(themeId, "themeId");
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    private void validateName(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException(name, fieldName);
        }
    }

    private void validateId(Long id, String fieldName) {
        if (id < 0) {
            throw new BadRequestException(id.toString(), fieldName);
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
