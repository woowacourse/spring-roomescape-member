package roomescape.reservation.dto.reservation;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotNull(message = "날짜가 입력되지 않았습니다.") LocalDate date,
        @NotNull(message = "시간이 입력되지 않았습니다.") Long timeId,
        @NotNull(message = "테마가 입력되지 않았습니다.") Long themeId
) {
}
