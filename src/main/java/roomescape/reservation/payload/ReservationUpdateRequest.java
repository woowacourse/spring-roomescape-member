package roomescape.reservation.payload;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUpdateRequest(
        @NotNull
        LocalDate date,
        @NotNull
        Long timeId,
        @NotNull
        Long themeId
) {

    public boolean isEmpty() {
        return date == null && timeId == null && themeId == null;
    }
}
