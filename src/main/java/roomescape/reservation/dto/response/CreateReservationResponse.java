package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.reservation.model.Reservation;

public record CreateReservationResponse(Long id, String name, LocalDate date, CreateTimeOfReservationsResponse time, CreateThemeOfReservationResponse theme) {
    public static CreateReservationResponse of(final Reservation reservation) {
        return new CreateReservationResponse(
                reservation.getId(),
                reservation.getName().getValue(),
                reservation.getDate(),
                CreateTimeOfReservationsResponse.of(reservation.getReservationTime()),
                CreateThemeOfReservationResponse.of(reservation.getTheme())
        );
    }
}
