package roomescape.domain.dto;

import roomescape.exception.ErrorType;
import roomescape.exception.InvalidClientRequestException;

import java.time.LocalDate;

public record ReservationRequest(LocalDate date, Long timeId, Long themeId, Long memberId) {

    public ReservationRequest {
        isValid(date, timeId, themeId, memberId);
    }

    public ReservationRequest with(Long memberId) {
        return new ReservationRequest(date, timeId, themeId, memberId);
    }

    private void isValid(final LocalDate date, final Long timeId, final Long themeId, final Long memberId) {
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
