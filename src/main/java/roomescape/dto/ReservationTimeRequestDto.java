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
            throw new IllegalArgumentException("잘못된 startAt 입력입니다.");
        }
    }

    public ReservationTime toTime() {
        return new ReservationTime(startAt);
    }
}
