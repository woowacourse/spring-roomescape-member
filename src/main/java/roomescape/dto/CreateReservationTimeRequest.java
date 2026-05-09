package roomescape.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "예약 시간 생성 요청")
public record CreateReservationTimeRequest(
        @Schema(description = "시작 시각 (HH:mm)", example = "10:00")
        String startAt
) {
}
