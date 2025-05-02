package roomescape.reservation.dto.request;

import roomescape.reservation.entity.Time;

import java.time.LocalTime;

public record TimeRequest(
        LocalTime startAt
) {

    public TimeRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("값이 입력되지 않았습니다.");
        }
    }

    public Time toEntity() {
        return new Time(0L, startAt);
    }
}
