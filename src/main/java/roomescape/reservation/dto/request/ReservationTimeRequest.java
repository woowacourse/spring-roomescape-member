package roomescape.reservation.dto.request;

import java.time.LocalTime;
import roomescape.reservation.entity.ReservationTime;

public record ReservationTimeRequest(
        LocalTime startAt
) {

    public ReservationTimeRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("값이 입력되지 않았습니다.");
        }
    }

    public ReservationTime toEntity() {
        return new ReservationTime(0L, startAt);
    }
}
