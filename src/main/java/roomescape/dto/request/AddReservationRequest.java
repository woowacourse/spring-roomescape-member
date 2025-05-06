package roomescape.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record AddReservationRequest(
        @FutureOrPresent(message = "날짜는 현재보다 미래여야합니다.") LocalDate date,
        @NotNull(message = "예약 시간 ID는 필수입니다.") Long timeId,
        @NotNull(message = "테마 ID는 필수입니다.") Long themeId
) {

    public Reservation toReservation(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, member, date, reservationTime, theme);
    }
}
