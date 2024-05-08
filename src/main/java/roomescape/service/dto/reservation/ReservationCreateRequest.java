package roomescape.service.dto.reservation;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationCreateRequest(
        @NotNull(message = "사용자는 비어있을 수 없습니다.") Long memberId,
        @NotNull(message = "예약 날짜는 비어있을 수 없습니다.") LocalDate date,
        @NotNull(message = "예약 시간은 비어있을 수 없습니다.") Long timeId,
        @NotNull(message = "테마는 비어있을 수 없습니다.") Long themeId) {

    public Reservation toReservation(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(member, date, reservationTime, theme);
    }
}
