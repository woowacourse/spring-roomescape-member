package roomescape.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "예약 생성 요청")
public record CreateReservationRequest(
        @Schema(description = "예약자 이름", example = "홍길동")
        String name,

        @Schema(description = "테마 ID", example = "1")
        Long themeId,

        @Schema(description = "예약 날짜 (yyyy-MM-dd)", example = "2025-08-05")
        LocalDate date,

        @Schema(description = "예약 시간 ID", example = "2")
        Long timeId
) {
}
