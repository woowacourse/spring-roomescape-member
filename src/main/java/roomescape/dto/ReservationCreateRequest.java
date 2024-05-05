package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.IllegalReservationException;

public record ReservationCreateRequest(
        Long id,
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public ReservationCreateRequest {
        validateTimeID(timeId);
        validateThemeID(themeId);
        validateDate(date);
    }

    public static Reservation toReservation(final ReservationCreateRequest request, final ReservationTime time, final Theme theme) {
        return new Reservation(request.id(), request.name(), request.date(), time, theme);
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
