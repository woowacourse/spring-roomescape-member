package roomescape.reservation.dto.theme;

import roomescape.reservation.domain.ReservationTheme;

public record WeeklyThemeResponse(String name, String description, String thumbnail) {

    public WeeklyThemeResponse(ReservationTheme theme) {
        this(theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
