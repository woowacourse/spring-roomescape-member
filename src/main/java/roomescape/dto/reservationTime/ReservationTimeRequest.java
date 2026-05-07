package roomescape.dto.reservationTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeRequest(
    @NotNull(message = "시작 시간은 필수 입력값입니다.")
    @JsonFormat(pattern = "HH:mm")
    LocalTime startAt
) {
}
