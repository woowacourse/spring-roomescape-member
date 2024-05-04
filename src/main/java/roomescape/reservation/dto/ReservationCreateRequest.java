package roomescape.reservation.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record ReservationCreateRequest(
        @NotNull(message = "이름은 필수 입력 값입니다.") String name,
        @FutureOrPresent(message = "과거의 날짜를 입력할 수 없습니다.") LocalDate date,
        @NotNull Long themeId,
        @NotNull Long timeId
) {

    public Reservation toReservation(Theme theme, ReservationTime reservationTime) {
        return new Reservation(new Name(name), date, theme, reservationTime);
    }
}
