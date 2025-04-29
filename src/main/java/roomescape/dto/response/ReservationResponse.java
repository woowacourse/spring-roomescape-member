package roomescape.dto.response;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        @JsonProperty("time") ReservationTimeResponse reservationTimeResponse,
        @JsonProperty("theme") ThemeResponse themeResponse
) {

    public static ReservationResponse of(Reservation reservation, ReservationTimeResponse reservationTimeResponse,
                                         ThemeResponse themeResponse) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservationTimeResponse,
                themeResponse
        );
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
