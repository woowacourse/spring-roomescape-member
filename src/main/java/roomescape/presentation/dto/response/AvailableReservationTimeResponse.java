package roomescape.presentation.dto.response;

import java.time.LocalTime;
import roomescape.business.model.entity.ReservationTime;

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
