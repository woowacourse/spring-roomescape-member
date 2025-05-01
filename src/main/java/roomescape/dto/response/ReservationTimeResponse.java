package roomescape.dto.response;

import java.time.LocalTime;
import roomescape.entity.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        LocalTime startAt
) {
    public static ReservationTimeResponse of(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}
