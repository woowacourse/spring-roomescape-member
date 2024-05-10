package roomescape.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.reservation.ReservationTime;

import java.time.LocalTime;

public record AvailableReservationTimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm") LocalTime startAt,
        Boolean alreadyBooked
) {
    public static AvailableReservationTimeResponse from(ReservationTime reservationTime, Boolean alreadyBooked) {
        return new AvailableReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                alreadyBooked);
    }
}
