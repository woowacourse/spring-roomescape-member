package roomescape.reservation.dto.time;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record TimeRequest(@NotNull(message = "시간이 입력되지 않았습니다.") LocalTime startAt) {
}
