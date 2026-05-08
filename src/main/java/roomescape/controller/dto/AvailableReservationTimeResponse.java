package roomescape.controller.dto;

import roomescape.domain.AvailableTime;

public record AvailableReservationTimeResponse(long id, String startAt, boolean isAvailable) {
    public static AvailableReservationTimeResponse from(AvailableTime availableTime) {
        return new AvailableReservationTimeResponse(
                availableTime.time().getId(),
                availableTime.time().getStartAt().toString(),
                availableTime.available()
        );
    }
}
