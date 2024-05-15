package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.reservation.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeResponse(
        long id,

        @JsonFormat(pattern = "kk:mm")
        LocalTime startAt
) {
    public ReservationTimeResponse(final ReservationTime reservationTime) {
        this(reservationTime.getId(), reservationTime.getStartAt());
    }
}
