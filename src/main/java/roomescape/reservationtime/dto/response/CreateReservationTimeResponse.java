package roomescape.reservationtime.dto.response;

import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record CreateReservationTimeResponse(
        Long id,
        LocalTime startAt
) {
    public static CreateReservationTimeResponse from(ReservationTime time) {
        return new CreateReservationTimeResponse(
                time.getId(),
                time.getStartAt()
        );
    }
}
