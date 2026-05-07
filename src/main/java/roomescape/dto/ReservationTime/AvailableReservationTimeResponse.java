package roomescape.dto.ReservationTime;

import roomescape.domain.ReservationTime.ReservationTimeWithAvailable;

public record AvailableReservationTimeResponse(long id, String startAt, boolean isAvailable) {
    public static AvailableReservationTimeResponse from(ReservationTimeWithAvailable reservationTimeWithAvailable) {
        return new AvailableReservationTimeResponse(reservationTimeWithAvailable.id(), reservationTimeWithAvailable.startAt(), reservationTimeWithAvailable.isAvailable());
    }
}
