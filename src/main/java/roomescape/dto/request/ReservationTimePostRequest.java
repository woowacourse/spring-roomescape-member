package roomescape.dto.request;

import java.time.LocalTime;
import roomescape.entity.ReservationTime;

public record ReservationTimePostRequest(
        LocalTime startAt
) {
    public ReservationTimePostRequest {
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
