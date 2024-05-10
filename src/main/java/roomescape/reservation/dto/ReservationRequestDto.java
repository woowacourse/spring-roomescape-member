package roomescape.reservation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import roomescape.auth.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public record ReservationRequestDto(
        @NotBlank(message = "예약날짜를 입력해야 합니다.") String date,
        @Min(value = 1, message = "올바른 예약 시간 ID를 입력해야 합니다.") long timeId,
        @Min(value = 1, message = "올바른 예약 테마 ID를 입력해야 합니다.") long themeId
) {
    public Reservation toReservation(final Member member, final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(member, new ReservationDate(date), reservationTime, theme);
    }
}
