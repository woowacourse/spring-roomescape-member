package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.reservation.dto.response.ReservationTimeResponse.ReservationTimeCreateResponse;
import roomescape.reservation.dto.response.ReservationTimeResponse.ReservationTimeReadResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.theme.dto.response.ThemeResponse.ThemeCreateResponse;
import roomescape.theme.dto.response.ThemeResponse.ThemeReadResponse;
import roomescape.theme.entity.Theme;

public class ReservationResponse {

    public record ReservationCreateResponse(
            Long id,
            String name,
            LocalDate date,
            ReservationTimeCreateResponse time,
            ThemeCreateResponse theme
    ) {

        public static ReservationCreateResponse from(Reservation reservation, Theme theme) {
            return new ReservationCreateResponse(
                    reservation.getId(),
                    reservation.getName(),
                    reservation.getDate(),
                    ReservationTimeCreateResponse.from(reservation.getTime()),
                    ThemeCreateResponse.from(theme)
            );
        }
    }

    public record ReservationReadResponse(
            Long id,
            String name,
            LocalDate date,
            ReservationTimeReadResponse time,
            ThemeReadResponse theme
    ) {

        public static ReservationReadResponse from(Reservation reservation, Theme theme) {
            return new ReservationReadResponse(
                    reservation.getId(),
                    reservation.getName(),
                    reservation.getDate(),
                    ReservationTimeReadResponse.from(reservation.getTime()),
                    ThemeReadResponse.from(theme)
            );
        }
    }
}
