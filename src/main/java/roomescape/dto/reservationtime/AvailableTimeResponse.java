package roomescape.dto.reservationtime;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record AvailableTimeResponse(Long id, LocalTime startAt, boolean alreadyBooked) {
    public static AvailableTimeResponse of(ReservationTime time, boolean alreadyBooked) {
        return new AvailableTimeResponse(time.getId(), time.getStartAt(), alreadyBooked);
    }
}
