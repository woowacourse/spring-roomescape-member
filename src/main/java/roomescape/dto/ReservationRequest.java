package roomescape.dto;

import java.time.LocalDate;
import roomescape.exception.IllegalReservationException;

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
            throw new IllegalArgumentException("[ERROR] 시간은 비어있을 수 없습니다.");
        }
    }

    private void validateThemeID(final Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("[ERROR] 테마 Id는 비어있을 수 없습니다.");
        }
    }

    private void validateDate(final LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalReservationException("[ERROR] 과거 날짜는 예약할 수 없습니다.");
        }
    }
}
