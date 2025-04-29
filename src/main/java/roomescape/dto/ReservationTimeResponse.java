package roomescape.dto;

import roomescape.entity.ReservationTimeEntity;

public record ReservationTimeResponse(Long id, String startAt) {
    public static ReservationTimeResponse from(ReservationTimeEntity entity) {
        return new ReservationTimeResponse(entity.getId(), entity.getFormattedTime());
    }
}
