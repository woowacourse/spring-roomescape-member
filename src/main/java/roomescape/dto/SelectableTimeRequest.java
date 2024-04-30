package roomescape.dto;

import java.time.LocalDate;

public record SelectableTimeRequest(
        LocalDate date,
        long themeId
) {
}
