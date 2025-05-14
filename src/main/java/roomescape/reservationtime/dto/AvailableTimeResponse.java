package roomescape.reservationtime.dto;

import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record AvailableTimeResponse(Long id, LocalTime startAt, boolean alreadyBooked) {
    public static AvailableTimeResponse of(ReservationTime time, boolean alreadyBooked) {
        return new AvailableTimeResponse(time.getId(), time.getStartAt(), alreadyBooked);
    }
}
