package roomescape.application.dto;

import java.time.LocalDate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(
        LocalDate date,
        long timeId,
        long themeId
) {

    public ReservationRequest {
        validateNotNull(date, timeId, themeId);
    }

    public Reservation toReservationWith(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                date,
                reservationTime,
                theme,
                member
        );
    }

    private void validateNotNull(LocalDate date, long timeId, long themeId) {
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
