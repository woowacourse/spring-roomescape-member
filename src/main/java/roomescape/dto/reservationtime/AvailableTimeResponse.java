package roomescape.dto.reservationtime;

import java.time.LocalTime;
import roomescape.entity.ReservationTimeEntity;

public record AvailableTimeResponse(Long id, LocalTime startAt, boolean alreadyBooked) {
    public static AvailableTimeResponse of(ReservationTimeEntity timeEntity, boolean alreadyBooked) {
        return new AvailableTimeResponse(timeEntity.id(), timeEntity.startAt(), alreadyBooked);
    }
}
