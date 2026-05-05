package roomescape.controller.dto;

import java.time.LocalTime;
import roomescape.domain.Theme;

public record AvailableTimeResponse(
        LocalTime startAt,
        boolean available
) {

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail());
    }
}
