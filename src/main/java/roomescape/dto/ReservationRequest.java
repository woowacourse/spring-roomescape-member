package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ReservationRequest(
        @NotBlank
        String name,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull
        Long themeId,
        @NotNull
        Long timeId) {
}
