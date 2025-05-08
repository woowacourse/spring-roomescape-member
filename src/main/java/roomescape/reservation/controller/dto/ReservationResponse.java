package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.time.controller.dto.ReservationTimeResponse;

public record ReservationResponse(
        Long id,
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()), ThemeResponse.from(reservation.getTheme()));
    }

}
