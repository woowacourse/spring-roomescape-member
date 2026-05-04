package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record TimeResponse(
    Long id,
    LocalTime startAt
) {

    public static TimeResponse from(ReservationTime saved) {
        return new TimeResponse(
            saved.getId(),
            saved.getStartAt()
        );
    }
}
