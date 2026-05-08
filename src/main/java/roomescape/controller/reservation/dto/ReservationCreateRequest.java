package roomescape.controller.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationCreateRequest(
        String name,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        Long themeId,

        Long timeId
) {
}
