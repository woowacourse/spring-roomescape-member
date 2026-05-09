package roomescape.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "테마별 예약 가능 시간 응답")
public record ThemeReservationTimeResponse(
        @Schema(description = "시간 ID", example = "1")
        Long id,

        @Schema(description = "시작 시각 (HH:mm)", example = "10:00")
        String startAt,

        @Schema(description = "이미 예약된 시간 여부", example = "false")
        boolean isReserved
) {
}
