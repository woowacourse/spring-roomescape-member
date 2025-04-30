package roomescape.dto.response;

import java.time.LocalDate;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

}
