package roomescape.reservation.presentation.response;

import java.time.LocalTime;
import roomescape.reservation.business.domain.ReservationTime;

public record AvailableReservationTimeResponse(
        Long id,
        LocalTime startAt,
        boolean isBooked
) {

    public static AvailableReservationTimeResponse from(ReservationTime reservationTime, boolean isBooked) {
        return new AvailableReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                isBooked);
    }
}
