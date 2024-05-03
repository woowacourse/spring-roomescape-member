package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationTimeWithBookStatusRequest(
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull
        Long themeId) {
}
