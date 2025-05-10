package roomescape.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.reservation.ReservationTime;

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
