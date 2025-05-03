package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.exception.EmptyValueException;
import roomescape.exception.ExceptionCause;

public record ReservationCreateRequest(
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public ReservationCreateRequest {
        validateFields(name, date, timeId, themeId);
    }

    private static void validateFields(String name, LocalDate date, Long timeId, Long themeId) {
        if (name.isBlank()) {
            throw new EmptyValueException(ExceptionCause.EMPTY_VALUE_RESERVATION_NAME);
        }
        if (date == null) {
            throw new EmptyValueException(ExceptionCause.EMPTY_VALUE_RESERVATION_DATE);
        }
        if (timeId == null) {
            throw new EmptyValueException(ExceptionCause.EMPTY_VALUE_RESERVATION_TIME);
        }
        if (themeId == null) {
            throw new EmptyValueException(ExceptionCause.EMPTY_VALUE_THEME);
        }
    }
}
