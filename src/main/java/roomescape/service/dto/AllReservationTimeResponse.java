package roomescape.service.dto;

import roomescape.domain.ReservationTime;

public record AllReservationTimeResponse(long id, String startAt) {
    public AllReservationTimeResponse(final ReservationTime reservationTime) {
        this(reservationTime.getId(), reservationTime.getStartAt());
    }
}
