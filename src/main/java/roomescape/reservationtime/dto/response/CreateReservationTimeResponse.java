package roomescape.reservationtime.dto.response;

import roomescape.reservationtime.model.ReservationTime;
import roomescape.util.CustomDateTimeFormatter;

public record CreateReservationTimeResponse(Long id, String startAt) {
    public static CreateReservationTimeResponse of(final ReservationTime reservationTime) {
        return new CreateReservationTimeResponse(
                reservationTime.getId(),
                CustomDateTimeFormatter.getFormattedTime(reservationTime.getTime()));
    }
}
