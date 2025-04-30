package roomescape.controller.dto;

import java.time.LocalDate;

public record AvailableReservationTimeSearchRequest(
        LocalDate date,
        Long themeId
) {
}
