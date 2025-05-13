package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.time.entity.ReservationTime;

import java.time.LocalTime;

public record ReservationAvailableTimeResponse(
        long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        boolean isBooked
) {

    public static ReservationAvailableTimeResponse of(final ReservationTime reservation, final boolean isBooked) {
        return new ReservationAvailableTimeResponse(reservation.getId(), reservation.getStartAt(), isBooked);
    }
}
