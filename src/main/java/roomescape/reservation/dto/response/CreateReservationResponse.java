package roomescape.reservation.dto.response;

import roomescape.reservation.model.Reservation;
import roomescape.util.CustomDateTimeFormatter;

public record CreateReservationResponse(Long id, String name, String date, CreateTimeOfReservationsResponse time) {
    public static CreateReservationResponse of(final Reservation reservation) {
        return new CreateReservationResponse(
                reservation.getId(),
                reservation.getName(),
                CustomDateTimeFormatter.getFormattedDate(reservation.getDate()),
                CreateTimeOfReservationsResponse.of(reservation.getReservationTime())
        );
    }
}
