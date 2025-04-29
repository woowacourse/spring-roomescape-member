package roomescape.time.dto;

import roomescape.time.entity.ReservationTimeEntity;

import java.time.LocalTime;

public record ReservationTimeRequest(LocalTime startAt) {
    public ReservationTimeRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("값이 입력되지 않았습니다.");
        }
    }

    public ReservationTimeEntity toEntity() {
        return new ReservationTimeEntity(0L, startAt);
    }
}
