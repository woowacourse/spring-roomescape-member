package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain_entity.Reservation;
import roomescape.domain_entity.ReservationTime;
import roomescape.domain_entity.Theme;

public record ReservationRequest(
        String name, LocalDate date, long timeId, Long themeId
) {
    public ReservationRequest {
        validateNotNull(name, date, timeId, themeId);
    }

    private void validateNotNull(String name, LocalDate date, long timeId, Long themeId) {
        if (name == null) {
            throw new IllegalArgumentException("잘못된 name 입력입니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("잘못된 date 입력입니다.");
        }
        if (timeId < 1) {
            throw new IllegalArgumentException("잘못된 timeId 입력입니다.");
        }
        if (themeId == null || themeId < 1) {
            throw new IllegalArgumentException("잘못된 themeId 입력입니다.");
        }
    }

    public Reservation toReservationWith(ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                name, date, reservationTime, theme
        );
    }
}
