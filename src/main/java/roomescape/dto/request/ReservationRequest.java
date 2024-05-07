package roomescape.dto.request;

import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {
    public ReservationRequest {
        isValid(name, date, timeId, themeId);
    }

    public Reservation toEntity(final Long id, final TimeSlot time, final Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    private void isValid(final String name, final LocalDate date, final Long timeId, final Long themeId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 이름은 비워둘 수 없습니다.");
        }

        if (date == null || date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 예약 날짜입니다.");
        }

        if (timeId == null || timeId <= 0) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 예약 시간입니다.");
        }

        if (themeId == null || themeId <= -1) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 테마 입니다.");
        }
    }
}
