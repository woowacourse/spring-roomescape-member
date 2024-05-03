package roomescape.reservationtime.dto.response;

import java.time.LocalTime;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.util.CustomDateTimeFormatter;

public record CreateReservationTimeResponse(Long id, LocalTime startAt) {
    public static CreateReservationTimeResponse of(final ReservationTime reservationTime) {
        return new CreateReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getTime());
    }
}
