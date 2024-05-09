package roomescape.dto.theme;

import java.time.LocalTime;
import roomescape.domain.time.ReservationTime;

public record AvailableTimeResponse(long id, LocalTime startAt, boolean alreadyBooked) {
    public static AvailableTimeResponse from(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableTimeResponse(reservationTime.getId(), reservationTime.getStartAt(), alreadyBooked);
    }
}
