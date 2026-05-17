package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationPatchRequest(
        @NotNull(message = "날짜는 빈 값일 수 없습니다.") LocalDate date,
        @NotNull(message = "timeId는 빈 값일 수 없습니다.") Long timeId) {
}
