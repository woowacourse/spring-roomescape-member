package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeRequestDto(
        @NotNull(message = "[ERROR] 시간은 비어 있을 수 없습니다.")
        LocalTime startAt
) {
    public ReservationTime toEntity() {
        return new ReservationTime(startAt);
    }
}
