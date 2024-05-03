package roomescape.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record ReservationTimeUserWebResponse(
    Long id,
    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    boolean alreadyBooked) {

}
