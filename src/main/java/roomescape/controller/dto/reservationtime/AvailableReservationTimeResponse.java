package roomescape.controller.dto.reservationtime;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.service.dto.AvailableReservationTimeResult;

public record AvailableReservationTimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        boolean available
) {

    public static AvailableReservationTimeResponse from(AvailableReservationTimeResult result) {
        return new AvailableReservationTimeResponse(result.id(), result.startAt(), result.available());
    }
}
