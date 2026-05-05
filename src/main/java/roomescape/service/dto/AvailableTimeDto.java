package roomescape.service.dto;

import roomescape.domain.ReservationTime;

public record AvailableTimeDto(
        ReservationTime time,
        boolean available
) {
}
