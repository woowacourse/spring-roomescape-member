package roomescape.service.dto;

import java.time.LocalTime;
import roomescape.controller.dto.request.CreateReservationTimeRequest;

public record ReservationTimeCreation(LocalTime startAt) {

    public static ReservationTimeCreation from(CreateReservationTimeRequest request) {
        return new ReservationTimeCreation(request.startAt());
    }
}
