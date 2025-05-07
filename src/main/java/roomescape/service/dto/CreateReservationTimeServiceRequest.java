package roomescape.service.dto;

import java.time.LocalTime;
import roomescape.controller.dto.request.CreateReservationTimeRequest;

public record CreateReservationTimeServiceRequest(LocalTime startAt) {

    public static CreateReservationTimeServiceRequest from(final CreateReservationTimeRequest request) {
        return new CreateReservationTimeServiceRequest(request.startAt());
    }
}
