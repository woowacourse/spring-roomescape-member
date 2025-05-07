package roomescape.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record ReservationResponse(
        long id,
        String name,
        @Schema(type = "string")
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

}
