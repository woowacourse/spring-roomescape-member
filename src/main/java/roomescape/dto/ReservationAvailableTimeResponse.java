package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.entity.ReservationTime;

public record ReservationAvailableTimeResponse(
        long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        boolean isBooked
) {

    public static ReservationAvailableTimeResponse of(final ReservationTime reservation, final boolean isBooked) {
        return new ReservationAvailableTimeResponse(reservation.id(), reservation.startAt(), isBooked);
    }
}
