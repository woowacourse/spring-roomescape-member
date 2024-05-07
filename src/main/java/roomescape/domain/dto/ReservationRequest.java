package roomescape.domain.dto;

import roomescape.exception.ErrorType;
import roomescape.exception.InvalidClientRequestException;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {

    public ReservationRequest {
        isValid(name, date, timeId, themeId);
    }

    private void isValid(final String name, final LocalDate date, final Long timeId, final Long themeId) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, "name", name);
        }

        if (date == null) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, "date", "");
        }

        if (timeId == null) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, "timeId", "");
        }

        if (timeId <= 0) {
            throw new InvalidClientRequestException(ErrorType.INVALID_TIME, "timeId", timeId.toString());
        }

        if (themeId == null) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, "themeId", "");
        }

        if (themeId <= 0) {
            throw new InvalidClientRequestException(ErrorType.INVALID_THEME, "themeId", themeId.toString());
        }
    }
}
