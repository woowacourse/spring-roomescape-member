package roomescape.dto;

import java.time.LocalTime;
import roomescape.model.ReservationTime;

public record TimeResponse(Long id, LocalTime startAt) {

    public static TimeResponse from(ReservationTime reservationTime) {
        return new TimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
