package roomescape.time.service.dto.response;

import roomescape.time.repository.dto.AvailableTimeResponse;

public record AvailableReservationTimeResponse(
        Long id,
        String startAt,
        boolean alreadyBooked
) {
    public static AvailableReservationTimeResponse from(AvailableTimeResponse response) {
        return new AvailableReservationTimeResponse(
                response.id(),
                response.startAt().toString(),
                response.alreadyBooked()
        );
    }
}
