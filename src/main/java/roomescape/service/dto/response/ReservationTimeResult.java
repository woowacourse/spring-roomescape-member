package roomescape.service.dto.response;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeResult(
        Long id,
        LocalTime startAt
) {

    public static ReservationTimeResult from(final ReservationTime time) {
        return new ReservationTimeResult(
                time.getId(),
                time.getStartAt()
        );
    }
}
