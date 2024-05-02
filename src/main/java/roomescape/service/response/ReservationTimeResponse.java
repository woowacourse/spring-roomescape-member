package roomescape.service.response;

import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(Long id, String startAt) {

    public static ReservationTimeResponse from(ReservationTime time) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt().toString());
    }
}
