package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record TimeRequest(
        @NotNull(message = "이름은 빈 값일 수 없습니다.") LocalTime startAt) {
}
