package roomescape.dto;

import java.time.format.DateTimeFormatter;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {
    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }
}
