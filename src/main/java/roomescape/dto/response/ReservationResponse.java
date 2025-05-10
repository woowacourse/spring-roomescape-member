package roomescape.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record ReservationResponse(

        @Schema(description = "예약 ID", example = "1")
        long id,

        @Schema(type = "string")
        LocalDate date,

        ReservationTimeResponse time,
        ThemeResponse theme,
        MemberResponse member
) {

}
