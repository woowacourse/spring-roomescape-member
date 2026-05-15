package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTimeStatus;
import roomescape.entity.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        LocalTime startAt,
        boolean reserved
) {
    public static ReservationTimeResponse of(ReservationTimeStatus reservationTimeStatus) {
        return new ReservationTimeResponse(
                reservationTimeStatus.getReservationTime().getId(),
                reservationTimeStatus.getReservationTime().getStartAt(),
                reservationTimeStatus.isReserved()
        );
    }

    public static ReservationTimeResponse from(ReservationTime reservationTime, boolean reserved) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                reserved
        );
    }
}
