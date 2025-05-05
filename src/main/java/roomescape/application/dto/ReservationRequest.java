package roomescape.application.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(
        String name,
        LocalDate date,
        long timeId,
        long themeId
) {

    public ReservationRequest {
        validateNotNull(name, date, timeId, themeId);
    }

    public Reservation toReservationWith(ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                name,
                date,
                reservationTime,
                theme
        );
    }

    private void validateNotNull(String name, LocalDate date, long timeId, long themeId) {
        if (name == null) {
            throw new IllegalArgumentException("잘못된 name 입력입니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("잘못된 date 입력입니다.");
        }
        if (timeId < 1) {
            throw new IllegalArgumentException("잘못된 timeId 입력입니다.");
        }
        if (themeId < 1) {
            throw new IllegalArgumentException("잘못된 themeId 입력입니다.");
        }
    }
}
