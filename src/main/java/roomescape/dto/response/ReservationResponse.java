package roomescape.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record ReservationResponse(
        @Schema(description = "예약 ID", example = "1")
        long id,

        @Schema(description = "예약자 이름", example = "체체")
        String name,

        @Schema(type = "string")
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

}
