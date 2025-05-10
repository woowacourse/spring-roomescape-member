package roomescape.presentation.request;

import java.time.LocalDate;
import roomescape.service.param.CreateReservationParam;

public record CreateReservationRequest(
        LocalDate date,
        Long timeId,
        Long themeId
) {

    private static final String ERROR_MESSAGE_FORMAT = "예약 필수 정보가 누락되었습니다. %s: %s";

    public CreateReservationRequest {
        validateDate(date);
        validateTimeId(timeId);
        validateThemeId(themeId);
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_FORMAT, "date", date));
        }
    }

    private void validateTimeId(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_FORMAT, "timeId", timeId));
        }
    }

    private void validateThemeId(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_FORMAT, "themeId", themeId));
        }
    }

    public CreateReservationParam toServiceParam(Long memberId) {
        return new CreateReservationParam(date, timeId, themeId, memberId);
    }
}
