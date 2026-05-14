package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.InvalidRequestException;

public record ReservationUpdateRequest(
        LocalDate date,
        Long timeId
) {
    public ReservationUpdateRequest {
        validateUpdateRequestNotEmpty(date, timeId);
    }

    private static void validateUpdateRequestNotEmpty(LocalDate date, Long timeId) {
        if (date == null && timeId == null) {
            throw new InvalidRequestException(ErrorCode.RESERVATION_UPDATE_REQUEST_EMPTY);
        }
    }
}
