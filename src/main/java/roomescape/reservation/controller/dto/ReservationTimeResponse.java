package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.reservation.domain.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul") LocalTime startAt
) {

    public static ReservationTimeResponse from(ReservationTime time) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt());
    }

}
