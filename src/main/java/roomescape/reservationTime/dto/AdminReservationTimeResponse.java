package roomescape.reservationTime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.reservationTime.domain.ReservationTime;

public record AdminReservationTimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {
    public static AdminReservationTimeResponse from(ReservationTime time) {
        return new AdminReservationTimeResponse(time.getId(), time.getStartAt());
    }
}
