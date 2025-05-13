package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationSearchRequest(
        Long themeId,
        Long memberId,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateFrom,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateTo
) {
}
