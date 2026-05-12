package roomescape.domain.reservationTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationTimeCondition(

        @NotNull(message = "날짜는 필수입니다.")
        LocalDate date,

        @Min(value = 1, message = "themeId는 1 이상이어야 합니다.")
        long themeId
) {
}
