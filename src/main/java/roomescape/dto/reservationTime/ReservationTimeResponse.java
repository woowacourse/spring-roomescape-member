package roomescape.dto.reservationTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.reservationTime.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeResponse(

        long id,

        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.id(), reservationTime.startAt());
    }
}
