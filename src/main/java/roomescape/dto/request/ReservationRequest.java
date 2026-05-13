package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(
        @NotBlank(message = "예약자명은 필수값 입니다.")
        String name,

        @NotNull(message = "예약 날짜는 필수값 입니다.")
        LocalDate date,

        long timeId,
        long themeId

) {
    public Reservation toReservation(ReservationTime reservationTime, Theme theme) {
        return Reservation.createWithoutId(name, date, reservationTime, theme);
    }
}
