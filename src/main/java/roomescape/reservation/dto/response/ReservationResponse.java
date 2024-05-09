package roomescape.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.dto.ThemeResponse;

import java.time.LocalDate;

public record ReservationResponse(Long id, LocalDate date,
                                  @JsonProperty("time") ReservationTimeResponse reservationTimeResponse,
                                  @JsonProperty("theme") ThemeResponse themeResponse) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
