package roomescape.service.dto.request;

import java.time.LocalTime;
import roomescape.controller.dto.request.CreateReservationTimeRequest;

public record ReservationTimeCreation(LocalTime startAt) {

    public static ReservationTimeCreation from(final CreateReservationTimeRequest request) {
        return new ReservationTimeCreation(request.startAt());
    }
}
