package roomescape.dto.request;

import java.time.LocalTime;

import roomescape.domain.ReservationTime;

public record CreateReservationTimeRequest(
        LocalTime startAt
) {

    public CreateReservationTimeRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("시간은 비어있을 수 없습니다.");
        }
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
