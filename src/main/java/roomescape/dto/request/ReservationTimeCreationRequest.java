package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeCreationRequest(
        @NotNull(message = "[ERROR] 시간은 빈 값을 허용하지 않습니다.")
        LocalTime startAt) {
}
