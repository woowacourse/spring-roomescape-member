package roomescape.reservationTime.dto;

import java.time.LocalTime;
import roomescape.globalException.ResponseInvalidException;
import roomescape.reservationTime.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        LocalTime startAt
) {
    public ReservationTimeResponse {
        if(id == null || startAt == null){
            throw new ResponseInvalidException();
        }
    }

    public static ReservationTimeResponse from(final ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}
