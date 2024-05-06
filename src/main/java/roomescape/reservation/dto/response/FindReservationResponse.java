package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.reservation.model.Reservation;

public record FindReservationResponse(
        Long id,
        String name,
        LocalDate date,
        FindTimeOfReservationsResponse time,
        FindThemeOfReservationResponse theme) {

    public static FindReservationResponse from(final Reservation reservation) {
        return new FindReservationResponse(
                reservation.getId(),
                reservation.getName().getValue(),
                reservation.getDate(),
                FindTimeOfReservationsResponse.from(reservation.getReservationTime()),
                FindThemeOfReservationResponse.from(reservation.getTheme())
        );
    }

    public static FindReservationResponse of(final Long id, Reservation reservation) {
        return new FindReservationResponse(
                id,
                reservation.getName().getValue(),
                reservation.getDate(),
                FindTimeOfReservationsResponse.from(reservation.getReservationTime()),
                FindThemeOfReservationResponse.from(reservation.getTheme())
        );
    }
}
