package roomescape.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.service.response.ReservationTimeAppResponseWithBookable;

public record ReservationTimeUserWebResponse(
    Long id,
    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    boolean alreadyBooked) {

    public static ReservationTimeUserWebResponse from(ReservationTimeAppResponseWithBookable appResponse) {
        return new ReservationTimeUserWebResponse(appResponse.id(), appResponse.startAt(), appResponse.alreadyBooked());
    }
}
