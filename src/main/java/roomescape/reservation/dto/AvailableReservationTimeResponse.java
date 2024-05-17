package roomescape.reservation.dto;

import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.reservation.domain.ReservationTime;

public record AvailableReservationTimeResponse(
        Long id,
        @DateTimeFormat(pattern = "HH:mm") LocalTime startAt,
        boolean booked
) {

    public static AvailableReservationTimeResponse toResponse(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt(),
                alreadyBooked);
    }
}
