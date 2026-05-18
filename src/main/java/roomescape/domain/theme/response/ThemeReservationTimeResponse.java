package roomescape.domain.theme.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record ThemeReservationTimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        Boolean isAvailable
) {
}
