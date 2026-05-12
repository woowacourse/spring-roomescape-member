package roomescape.presentation.dto;

import java.time.LocalDate;
import java.util.Optional;

public record ReservationUpdateRequest(
        Optional<LocalDate> date,
        Optional<Long> timeId
) {
}
