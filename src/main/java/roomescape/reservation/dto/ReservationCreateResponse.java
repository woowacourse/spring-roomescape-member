package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.dto.ReservationTimeCreateResponse;
import roomescape.theme.dto.ThemeCreateResponse;

public record ReservationCreateResponse(
        Long id,
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        ReservationTimeCreateResponse time,
        ThemeCreateResponse theme
) {

    public static ReservationCreateResponse from(Reservation reservation) {
        return new ReservationCreateResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                ReservationTimeCreateResponse.from(reservation.getTime()),
                ThemeCreateResponse.from(reservation.getTheme()));
    }
}
