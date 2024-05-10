package roomescape.web.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(@NotNull(message = "날짜는 빈값을 허용하지 않습니다.") LocalDate date,
                                 @Min(value = 1, message = "타임 아이디는 1이상의 정수만 허용합니다.") long timeId,
                                 @Min(value = 1, message = "테마 아이디는 1이상의 정수만 허용합니다.") long themeId) {

    public Reservation toReservation(ReservationTime reservationTime, Theme theme) {
        return new Reservation(date, reservationTime, theme);
    }
}
