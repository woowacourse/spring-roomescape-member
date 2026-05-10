package roomescape.domain.theme.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.theme.repository.ThemeReservationTimeResult;

public record ThemeReservationTimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        Boolean isAvailable
) {

    public static ThemeReservationTimeResponse from(ThemeReservationTimeResult timeResult) {
        return new ThemeReservationTimeResponse(
                timeResult.id(),
                timeResult.startAt(),
                timeResult.isAvailable()
        );
    }
}
