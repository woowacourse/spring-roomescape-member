package roomescape.service.dto;

import roomescape.domain.ReservationTime;

public record TimeAvailabilityDto(
        ReservationTime time,
        boolean available
) {
}
