package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationResponse(
        Long id,
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation) {
        ReservationTimeResponse reservationTimeResponse = getReservationTimeResponse(reservation.getTime());
        ThemeResponse themeResponse = getThemeResponse(reservation.getTheme());

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservationTimeResponse,
                themeResponse
        );
    }

    private static ReservationTimeResponse getReservationTimeResponse(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }

    private static ThemeResponse getThemeResponse(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName().getName(),
                theme.getDescription().getDescription(),
                theme.getThumbnail().getThumbnail()
        );
    }
}
