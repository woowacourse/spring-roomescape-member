package roomescape.reservation.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.reservation.application.dto.ReservationQueryResult;
import roomescape.reservationtime.application.dto.ReservationTimeQueryResult;
import roomescape.theme.application.dto.ThemeQueryResult;

public record ReservationResponse(
        Long id,
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        ThemeQueryResult theme,
        ReservationTimeQueryResult time
) {
    public static ReservationResponse from(ReservationQueryResult result) {
        return new ReservationResponse(
                result.id(),
                result.name(),
                result.date(),
                result.theme(),
                result.time()
        );
    }
}
