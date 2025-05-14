package roomescape.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationCreateRequest(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        long timeId,
        long themeId
) {
}
