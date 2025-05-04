package roomescape.reservation_time.domain.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalTime;

public record ReservationTimeReqDto(
    @NotBlank(message = "예약 시작 시간은 필수입니다.")
    LocalTime startAt
) {
}
