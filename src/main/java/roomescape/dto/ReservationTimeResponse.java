package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        LocalTime startAt,
        boolean isNotReserved
) {
    public static ReservationTimeResponse from(ReservationTime reservationTime, boolean isNotReserved) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                isNotReserved);
    }
}
