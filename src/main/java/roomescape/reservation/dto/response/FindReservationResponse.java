package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.reservation.model.Reservation;

public record FindReservationResponse(
        Long id,
        String name,
        LocalDate date,
        FindTimeOfReservationsResponse time,
        FindThemeOfReservationResponse theme) {

    public static FindReservationResponse of(final Reservation reservation) {
        return new FindReservationResponse(
                reservation.getId(),
                reservation.getName().getValue(),
                reservation.getDate(),
                FindTimeOfReservationsResponse.of(reservation.getReservationTime()),
                FindThemeOfReservationResponse.of(reservation.getTheme())
        );
    }
}
