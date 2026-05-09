package roomescape.controller.dto;

import roomescape.service.dto.ServiceReservationTimeAvailabilityResponse;

public record ControllerReservationTimeAvailabilityResponse(
        ControllerReservationTimeResponse time,
        boolean available
) {
    public static ControllerReservationTimeAvailabilityResponse from(
            ServiceReservationTimeAvailabilityResponse response) {
        return new ControllerReservationTimeAvailabilityResponse(
                ControllerReservationTimeResponse.from(response.time()),
                response.available());
    }
}
