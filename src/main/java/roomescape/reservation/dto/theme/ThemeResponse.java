package roomescape.reservation.dto.theme;

import roomescape.reservation.domain.ReservationTheme;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {

    public ThemeResponse(ReservationTheme theme) {
        this(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
