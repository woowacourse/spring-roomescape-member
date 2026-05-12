package roomescape.dto.response;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record AvailableReservationTimeResponse(
        Long id,
        LocalTime startAt,
        boolean isReserved
) {
    public static AvailableReservationTimeResponse from(ReservationTime reservationTime, boolean isReserved) {
        return new AvailableReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                isReserved);
    }
}
