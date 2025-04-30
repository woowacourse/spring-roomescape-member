package roomescape.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationResponse(

        long id,

        String name,

        ThemeResponse theme,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        TimeResponse time
) {
}
