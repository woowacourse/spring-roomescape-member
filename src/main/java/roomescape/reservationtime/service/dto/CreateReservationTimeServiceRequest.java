package roomescape.reservationtime.service.dto;

import java.time.LocalTime;
import roomescape.reservationtime.controller.dto.CreateReservationTimeRequest;

public record CreateReservationTimeServiceRequest(LocalTime startAt) {

    public static CreateReservationTimeServiceRequest from(final CreateReservationTimeRequest request) {
        return new CreateReservationTimeServiceRequest(request.startAt());
    }
}
