package roomescape.domain.theme.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.reservationtime.entity.ReservationTime;

public record ThemeReservationTimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        Boolean isAvailable
) {

    public static ThemeReservationTimeResponse from(ReservationTime time, boolean isAvailable) {
        return new ThemeReservationTimeResponse(
                time.getId(),
                time.getStartAt(),
                isAvailable
        );
    }
}
