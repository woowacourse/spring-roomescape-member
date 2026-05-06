package roomescape.time.repository.dto;

import java.time.LocalDate;

public record FindReservedTimeParams (
        Long themeId,
        LocalDate date
) {
}
