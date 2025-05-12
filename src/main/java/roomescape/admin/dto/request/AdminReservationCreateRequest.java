package roomescape.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record AdminReservationCreateRequest(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        long memberId,
        long timeId,
        long themeId
) {
}
