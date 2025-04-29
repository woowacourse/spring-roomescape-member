package roomescape.time.dto;

import roomescape.time.entity.ReservationTimeEntity;

public record ReservationTimeResponse(Long id, String startAt) {
    public static ReservationTimeResponse from(ReservationTimeEntity entity) {
        return new ReservationTimeResponse(entity.getId(), entity.getFormattedTime());
    }
}
