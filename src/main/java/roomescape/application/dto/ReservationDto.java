package roomescape.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.presentation.dto.response.AdminThemeResponse;

public record ReservationDto(
        long id,
        String name,
        ThemeDto theme,
        LocalDate date,
        TimeDto time
) {
}
