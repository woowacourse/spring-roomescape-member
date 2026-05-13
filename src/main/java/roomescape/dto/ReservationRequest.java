package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank(message = "RESERVATION_NAME_BLANK")
        @Size(min = 2, max = 20, message = "RESERVATION_NAME_LENGTH_INVALID")
        String name,

        @NotNull(message = "RESERVATION_DATE_NULL")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "RESERVATION_TIME_NULL")
        Long timeId,

        @NotNull(message = "RESERVATION_THEME_NULL")
        Long themeId) {
}
