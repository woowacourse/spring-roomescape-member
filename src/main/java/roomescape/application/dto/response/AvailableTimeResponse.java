package roomescape.application.dto.response;

import roomescape.domain.dto.AvailableTimeDto;

public record AvailableTimeResponse(ReservationTimeResponse time, boolean isBooked) {

    public static AvailableTimeResponse from(AvailableTimeDto dto) {
        return new AvailableTimeResponse(
                new ReservationTimeResponse(dto.id(), dto.startAt()),
                dto.isBooked()
        );
    }
}
