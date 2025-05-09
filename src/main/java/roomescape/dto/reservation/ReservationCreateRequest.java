package roomescape.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationCreateRequest(
        @NotBlank(message = "[ERROR] 예약자의 이름은 1글자 이상으로 이루어져야 합니다.") String name,
        @NotNull(message = "[ERROR] 날짜가 없습니다.") LocalDate date,
        @NotNull(message = "[ERROR] 예약시간 id가 없습니다.")
        @PositiveOrZero(message = "[ERROR] id는 양수여야 합니다.") Long timeId,
        @NotNull(message = "[ERROR] 테마 id가 없습니다.")
        @PositiveOrZero(message = "[ERROR] id는 양수여야 합니다.") Long themeId
) {

    public Reservation createWithoutId(ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, name, date, reservationTime, theme);
    }
}
