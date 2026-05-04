package roomescape.reservations.presentation.dto;

import java.time.LocalDate;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ReservationException;
import roomescape.global.exception.customException.ReservationTimeException;

public record ReservationRequest(
        String name,
        LocalDate date,
        Long timeId
) {
    public ReservationRequest{
        validateNameNotEmpty(name);
        validateDateNotEmpty(date);
        validateTimeIdNotEmpty(timeId);
    }

    private static void validateNameNotEmpty(String name) {
        if (name == null || name.trim().isBlank()) {
            throw new ReservationException(ErrorCode.RESERVATION_NAME_EMPTY);
        }
    }
    
    private static void validateTimeIdNotEmpty(Long timeId) {
        if (timeId == null) {
            throw new ReservationTimeException(ErrorCode.RESERVATION_TIME_ID_NULL);
        }
    }

    private static void validateDateNotEmpty(LocalDate date) {
        if (date == null) {
            throw new ReservationException(ErrorCode.RESERVATION_DATE_NULL);
        }
    }


}
