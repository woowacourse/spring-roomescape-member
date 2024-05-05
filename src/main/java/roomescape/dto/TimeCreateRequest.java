package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record TimeCreateRequest(
        Long id,
        LocalTime startAt
) {

    public static ReservationTime toTime(final TimeCreateRequest request) {
        return new ReservationTime(request.id(), request.startAt());
    }
}
