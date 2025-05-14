package roomescape.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeCreateResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public static ReservationTimeCreateResponse from(ReservationTime reservationTime) {
        return new ReservationTimeCreateResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
