package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain_entity.ReservationTime;

public record ReservationTimeRequestDto(
        LocalTime startAt
) {
    public ReservationTimeRequestDto {
        validateNotNull(startAt);
    }

    private void validateNotNull(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("요청 필드가 올바르지 않습니다.");
        }
    }

    public ReservationTime toTime() {
        return new ReservationTime(startAt);
    }
}
