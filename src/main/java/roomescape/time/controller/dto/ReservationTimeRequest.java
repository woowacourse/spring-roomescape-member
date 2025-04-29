package roomescape.time.controller.dto;

import java.time.LocalTime;

public record ReservationTimeRequest(
        LocalTime startAt
) {

}
