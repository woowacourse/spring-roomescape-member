package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ReservationRequest(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull
        Long themeId,
        @NotNull
        Long timeId) {
}
