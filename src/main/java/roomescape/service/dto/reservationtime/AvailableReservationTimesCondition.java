package roomescape.service.dto.reservationtime;

import java.time.LocalDate;
import roomescape.global.exception.InvalidReservationTimeException;

public record AvailableReservationTimesCondition(
        Long themeId,
        LocalDate date,
        Boolean available
) {

    public AvailableReservationTimesCondition {
        if (themeId == null) {
            throw new InvalidReservationTimeException("테마 ID는 필수입니다.");
        }
        if (date == null) {
            throw new InvalidReservationTimeException("예약 날짜는 필수입니다.");
        }
    }
}
