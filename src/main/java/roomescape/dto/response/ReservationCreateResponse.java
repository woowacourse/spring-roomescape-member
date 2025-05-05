package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.domain.Reservation;

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
