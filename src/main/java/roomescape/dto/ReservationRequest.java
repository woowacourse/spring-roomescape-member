package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationRequest(
        String name,
        @JsonFormat(pattern = "uuuu-MM-dd")
        LocalDate date,
        Long timeId,
        Long themeId
) {
}
