package roomescape.reservation.dto.response;

import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.theme.dto.response.ThemeResponse;

public record CreateReservationResponse(
        Long id,
        String name,
        String date,
        ThemeResponse theme,
        CreateReservationTimeResponse time
) {
    public static CreateReservationResponse fromEntity(Reservation reservation) {
        return new CreateReservationResponse(
                reservation.getId(),
                reservation.getUser().getName(),
                reservation.getDate().toString(),
                ThemeResponse.from(reservation.getTheme()),
                CreateReservationTimeResponse.fromEntity(reservation.getReservationTime())
        );
    }
}
