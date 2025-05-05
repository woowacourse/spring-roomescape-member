package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationAvailableTimeResponse(
    Long id,
    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    boolean isBooked
) {

    public static ReservationAvailableTimeResponse from(ReservationTime time, boolean isBooked) {
        return new ReservationAvailableTimeResponse(time.getId(), time.getStartAt(), isBooked);
    }
}
