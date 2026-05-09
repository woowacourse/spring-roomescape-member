package roomescape.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import roomescape.domain.ReservationTime;

@Schema(description = "예약 시간 응답")
public record ReservationTimeResponse(
        @Schema(description = "시간 ID", example = "1")
        Long id,

        @Schema(description = "시작 시각 (HH:mm)", example = "10:00")
        String startAt
) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt().toString()
        );
    }
}
