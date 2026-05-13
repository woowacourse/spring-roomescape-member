package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record TimeRequest(
        @NotNull(message = "TIME_NULL")
        LocalTime startAt) {
}
