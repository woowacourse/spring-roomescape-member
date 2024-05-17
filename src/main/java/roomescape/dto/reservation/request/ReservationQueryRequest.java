package roomescape.dto.reservation.request;

import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ReservationQueryRequest(
        @Positive Long themeId,
        @Positive Long memberId,
        String dateFrom,
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo
) {
}
