package roomescape.service.command;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeCommand(
        @NotNull(message = "예약 시간 정보는 필수 값입니다.")
        LocalTime startAt
) {
}
