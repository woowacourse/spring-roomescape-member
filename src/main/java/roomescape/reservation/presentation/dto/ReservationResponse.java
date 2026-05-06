package roomescape.reservation.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.reservation.application.dto.ReservationQueryResult;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.theme.dto.ThemeResponse;

public record ReservationResponse(
        Long id,
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        ThemeResponse theme,
        ReservationTimeResponse time
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
