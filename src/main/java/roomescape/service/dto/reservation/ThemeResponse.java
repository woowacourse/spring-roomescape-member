package roomescape.service.dto.reservation;

import roomescape.domain.reservation.Theme;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {

    public static ThemeResponse of(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail());
    }
}
