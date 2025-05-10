package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.time.entity.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeResponse(
        long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public static ReservationTimeResponse from(final ReservationTime reservation) {
        return new ReservationTimeResponse(reservation.getId(), reservation.getStartAt());
    }
}
