package roomescape.application.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;

public record ReservationRequest(
        @NotBlank(message = "이름을 입력해주세요.")
        String name,
        @NotNull(message = "날짜를 입력해주세요.")
        LocalDate date,
        @NotNull(message = "시간 ID를 입력해주세요.")
        Long timeId,
        @NotNull(message = "테마 ID를 입력해주세요.")
        Long themeId) {

    public Reservation toReservation(ReservationTime reservationTime, Theme theme) {
        return new Reservation(name, date, reservationTime, theme);
    }
}
