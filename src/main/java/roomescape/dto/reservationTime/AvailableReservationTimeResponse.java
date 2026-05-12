package roomescape.dto.reservationTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;

import java.time.LocalTime;

public record AvailableReservationTimeResponse(
        long id,

        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,

        boolean isAvailable
) {

    public static AvailableReservationTimeResponse from(ReservationTimeWithAvailable reservationTimeWithAvailable) {
        return new AvailableReservationTimeResponse(reservationTimeWithAvailable.id(), reservationTimeWithAvailable.startAt(), reservationTimeWithAvailable.isAvailable());
    }
}
