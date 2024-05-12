package roomescape.dto.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.theme.ThemeResponse;
import roomescape.dto.time.TimeResponse;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        @JsonProperty("time") TimeResponse timeResponse,
        @JsonProperty("theme") ThemeResponse themeResponse
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMember().getName(),
                reservation.getDate(),
                TimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
