package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationCreateRequest(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        String name,
        Long timeId,
        Long themeId
) {
}
