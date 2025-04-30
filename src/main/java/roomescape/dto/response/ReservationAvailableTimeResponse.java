package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record ReservationAvailableTimeResponse(
    Long id,
    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    boolean isBooked
) {

}
