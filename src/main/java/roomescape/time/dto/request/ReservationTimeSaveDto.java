package roomescape.time.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeSaveDto(
        @NotNull(message = "예약 시작 시간은 필수입니다.")
        LocalTime startAt) {
}
