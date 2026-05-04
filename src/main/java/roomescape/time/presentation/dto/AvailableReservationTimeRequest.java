package roomescape.time.presentation.dto;

import java.time.LocalDate;

public record AvailableReservationTimeRequest(
        Long themeId,
        LocalDate date
) {
}
