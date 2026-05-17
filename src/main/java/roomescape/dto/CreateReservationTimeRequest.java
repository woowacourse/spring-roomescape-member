package roomescape.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import roomescape.exception.InvalidInputException;

@Schema(description = "예약 시간 생성 요청")
public record CreateReservationTimeRequest(
        @Schema(description = "시작 시각 (HH:mm)", example = "10:00")
        String startAt
) {
    public CreateReservationTimeRequest {
        if (startAt == null || startAt.isBlank()) {
            throw new InvalidInputException("예약 시간은 필수입니다.");
        }
    }
}
