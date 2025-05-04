package roomescape.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationCreateResponse(
        long id,
        String name,
        @Schema(type = "string")
        LocalDate date,
        ReservationTimeCreateResponse time,
        ThemeCreateResponse theme
) {

    public static ReservationCreateResponse from(final Reservation reservation) {
        return new ReservationCreateResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                ReservationTimeCreateResponse.from(reservation.getTime()),
                ThemeCreateResponse.from(reservation.getTheme()));
    }
}
