package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionCause;

public record ReservationCreateRequest(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public ReservationCreateRequest {
        validateFields(date, timeId, themeId);
    }

    private static void validateFields(LocalDate date, Long timeId, Long themeId) {
        if (date == null) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_RESERVATION_DATE);
        }
        if (timeId == null) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_RESERVATION_TIME);
        }
        if (themeId == null) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_THEME);
        }
    }
}
