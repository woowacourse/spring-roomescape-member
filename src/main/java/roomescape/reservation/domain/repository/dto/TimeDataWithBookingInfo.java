package roomescape.reservation.domain.repository.dto;

import java.time.LocalTime;

public record TimeDataWithBookingInfo(
        Long id,
        LocalTime startAt,
        boolean alreadyBooked
) {
}
