package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record TimeRequest(
        Long id,
        LocalTime startAt
) {

    public static ReservationTime toTime(final TimeRequest request) {
        return new ReservationTime(request.id(), request.startAt());
    }
}
