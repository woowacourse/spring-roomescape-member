package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm")
        @NotNull(message = "예약 시작 시간은 필수입니다.")
        LocalTime startAt
) {
}
