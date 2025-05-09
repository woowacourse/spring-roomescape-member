package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.domain.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        @JsonProperty("time") ReservationTimeResponse reservationTimeResponse,
        @JsonProperty("theme") ThemeResponse themeResponse
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
