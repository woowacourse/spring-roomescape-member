package roomescape.domain.reservation;

import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ReservationQuery(
        @Positive Long themeId,
        @Positive Long memberId,
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo
) {
}
