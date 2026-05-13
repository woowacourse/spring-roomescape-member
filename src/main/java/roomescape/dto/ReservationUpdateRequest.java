package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUpdateRequest(
        @NotNull(message = "RESERVATION_DATE_NULL")
        LocalDate date,

        @NotNull(message = "RESERVATION_TIME_NULL")
        Long timeId) {
}