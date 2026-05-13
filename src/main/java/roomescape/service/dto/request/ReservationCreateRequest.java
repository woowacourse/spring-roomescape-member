package roomescape.service.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ReservationCreateRequest(
        @NotNull
        String name,

        @DateTimeFormat(fallbackPatterns = "yyyy-mm-dd")
        @NotNull
        LocalDate date,

        @NotNull
        Long timeId,

        @NotNull
        Long themeId
) {
}
