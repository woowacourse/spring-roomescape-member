package roomescape.reservation.dto.response;

import roomescape.reservationtime.model.ReservationTime;
import roomescape.util.CustomDateTimeFormatter;

public record CreateTimeOfReservationsResponse(Long id, String startAt) {
    public static CreateTimeOfReservationsResponse of(final ReservationTime reservationTime) {
        return new CreateTimeOfReservationsResponse(
                reservationTime.getId(),
                CustomDateTimeFormatter.getFormattedTime(reservationTime.getTime())
        );
    }
}
