package roomescape.dto.request;

import java.time.LocalDate;
import roomescape.entity.Member;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;

public record ReservationPostRequestByUser(
        LocalDate date,
        Long timeId,
        Long themeId
) {
    public ReservationPostRequestByUser {
        validateNotNull(date, timeId, themeId);
    }

    private void validateNotNull(LocalDate date, Long timeId, Long themeId) {
        if (date == null) {
            throw new IllegalArgumentException("잘못된 date 입력입니다.");
        }
        if (timeId == null || timeId < 1) {
            throw new IllegalArgumentException("잘못된 timeId 입력입니다.");
        }
        if (themeId == null || themeId < 1) {
            throw new IllegalArgumentException("잘못된 themeId 입력입니다.");
        }
    }

    public Reservation toReservationWith(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                date, member, reservationTime, theme
        );
    }
}
