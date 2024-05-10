package roomescape.controller.request;

import java.time.LocalDate;

import roomescape.exception.BadRequestException;

public class ReservationRequest {
    private final LocalDate date;
    private final long timeId;
    private final long themeId;
    private final long userId;

    public ReservationRequest(LocalDate date, long timeId, long themeId, long userId) {
        validateId(timeId, "timeId");
        validateId(themeId, "themeId");
        validateId(userId, "userId");
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
        this.userId = userId;
    }

    private void validateId(Long id, String fieldName) {
        if (id <= 0) {
            throw new BadRequestException(id.toString(), fieldName);
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public long getTimeId() {
        return timeId;
    }

    public long getThemeId() {
        return themeId;
    }

    public long getUserId() {
        return userId;
    }
}
