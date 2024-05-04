package roomescape.service.dto;

import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(long id, String startAt) {
    public ReservationTimeResponse(final ReservationTime reservationTime) {
        this(reservationTime.getId(), reservationTime.getStartAt());
    }
}
