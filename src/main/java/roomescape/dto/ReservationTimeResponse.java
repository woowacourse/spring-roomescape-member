package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.entity.ReservationTime;

public record ReservationTimeResponse(
        long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public static ReservationTimeResponse from(final ReservationTime reservation) {
        return new ReservationTimeResponse(reservation.id(), reservation.startAt());
    }
}
