package roomescape.reservation.dto.response;

import roomescape.reservation.model.Reservation;
import roomescape.util.CustomDateTimeFormatter;

public record FindReservationResponse(Long id, String name, String date, FindTimeOfReservationsResponse time) {

    public static FindReservationResponse of(final Reservation reservation) {
        return new FindReservationResponse(
                reservation.getId(),
                reservation.getName(),
                CustomDateTimeFormatter.getFormattedDate(reservation.getDate()),
                FindTimeOfReservationsResponse.of(reservation.getReservationTime())
        );
    }
}
