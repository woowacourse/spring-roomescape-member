package roomescape.dto.response;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record AvailableReservationTimeResponse(
        Long id,
        LocalTime startAt,
        boolean isNotReserved
) {
    public static AvailableReservationTimeResponse from(ReservationTime reservationTime, boolean isNotReserved) {
        return new AvailableReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                isNotReserved);
    }
}
