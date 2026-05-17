package roomescape.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import roomescape.exception.InvalidInputException;

@Schema(description = "예약 생성 요청")
public record CreateReservationRequest(
        @Schema(description = "예약자 사용자 ID", example = "1")
        Long userId,

        @Schema(description = "테마 ID", example = "1")
        Long themeId,

        @Schema(description = "예약 날짜 (yyyy-MM-dd)", example = "2025-08-05")
        LocalDate date,

        @Schema(description = "예약 시간 ID", example = "2")
        Long timeId
) {
    public CreateReservationRequest {
        if (userId == null || userId <= 0) {
            throw new InvalidInputException("사용자 ID는 필수입니다.");
        }
        if (themeId == null || themeId <= 0) {
            throw new InvalidInputException("테마 ID는 필수입니다.");
        }
        if (date == null) {
            throw new InvalidInputException("예약 날짜는 필수입니다.");
        }
        if (timeId == null || timeId <= 0) {
            throw new InvalidInputException("시간 ID는 필수입니다.");
        }
    }
}
