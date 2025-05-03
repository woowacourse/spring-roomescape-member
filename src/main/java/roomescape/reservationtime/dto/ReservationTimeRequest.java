package roomescape.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.exception.custom.reason.RequestInvalidException;

public record ReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm") LocalTime startAt
) {
    public ReservationTimeRequest {
        if (startAt == null) {
            throw new RequestInvalidException();
        }
    }
}
