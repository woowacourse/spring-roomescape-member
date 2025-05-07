package roomescape.service.dto;

import java.time.LocalTime;

public record CreateReservationTimeRequest(LocalTime startAt) {

    public static CreateReservationTimeRequest from(
            final roomescape.controller.dto.request.CreateReservationTimeRequest request) {
        return new CreateReservationTimeRequest(request.startAt());
    }
}
