package roomescape.time.service.dto.response;

import roomescape.time.entity.ReservationTime;

public record ReservationTimeResponse(Long id, String startAt) {
    public static ReservationTimeResponse from(ReservationTime entity) {
        return new ReservationTimeResponse(entity.getId(), entity.getFormattedTime());
    }
}
