package roomescape.domain.reservation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ReservationUpdateRequest(

        @NotNull(message = "themeId는 null일 수 없습니다.")
        @Positive(message = "themeId는 양수만 가능합니다.")
        Long themeId,

        @NotNull(message = "date은 null일 수 없습니다.")
        LocalDate date,

        @NotNull(message = "timeId는 null일 수 없습니다.")
        @Positive(message = "timeId는 양수만 가능합니다.")
        Long timeId
) {
}
