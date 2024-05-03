package roomescape.dto;

import java.time.LocalDate;
import roomescape.exception.IllegalDateException;
import roomescape.exception.IllegalThemeException;
import roomescape.exception.IllegalTimeException;

public record ReservationRequest(
        Long id,
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public ReservationRequest {
        validateTimeID(timeId);
        validateThemeID(themeId);
        validateDate(date);
    }

    private void validateTimeID(final Long timeId) {
        if (timeId == null) {
            throw new IllegalTimeException("[ERROR] 유효하지 않은 형식의 예약 시간입니다.");
        }
    }

    private void validateThemeID(final Long themeId) {
        if (themeId == null) {
            throw new IllegalThemeException("[ERROR] 유효하지 않은 형식의 테마입니다.");
        }
    }

    private void validateDate(final LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalDateException("[ERROR] 이미 지난 날짜입니다.");
        }
    }
}
