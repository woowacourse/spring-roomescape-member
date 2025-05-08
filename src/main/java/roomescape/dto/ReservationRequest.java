package roomescape.dto;

import java.time.LocalDate;
import roomescape.entity.Member;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;

public record ReservationRequest(
        String name, LocalDate date, long timeId, long themeId
) {
    public ReservationRequest {
        validateNotNull(name, date, timeId, themeId);
    }

    private void validateNotNull(String name, LocalDate date, Long timeId, Long themeId) {
        //todo : long으로 가능하면 바꾸기
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("잘못된 name 입력입니다.");
        }
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
                name, date, member, reservationTime, theme
        );
    }
}
