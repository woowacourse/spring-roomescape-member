package roomescape.reservation.dto;

import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.reservation.domain.ReservationTime;

public record TimeResponse(
        Long id,
        @DateTimeFormat(pattern = "HH:mm") LocalTime startAt
) {

    public static TimeResponse toResponse(ReservationTime reservationTime) {
        return new TimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
