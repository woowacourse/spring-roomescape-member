package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.reservation.model.Reservation;

public record CreateReservationResponse(Long id, String name, LocalDate date, CreateTimeOfReservationsResponse time, CreateThemeOfReservationResponse theme) {
    public static CreateReservationResponse from(final Reservation reservation) {
        return new CreateReservationResponse(
                reservation.getId(),
                reservation.getName().getValue(),
                reservation.getDate(),
                CreateTimeOfReservationsResponse.from(reservation.getReservationTime()),
                CreateThemeOfReservationResponse.from(reservation.getTheme())
        );
    }
}
