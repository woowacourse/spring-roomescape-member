package roomescape.reservation.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record ReservationRequest(
        @NotNull(message = "이름을 입력해주세요.") String name,
        @NotNull(message = "날짜를 입력해주세요.") LocalDate date,
        @NotNull(message = "시간을 입력해주세요.") Long timeId,
        @NotNull(message = "테마를 입력해주세요.") Long themeId
) {

    public Reservation convertToReservation(final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(null, name, date, reservationTime, theme);
    }
}
