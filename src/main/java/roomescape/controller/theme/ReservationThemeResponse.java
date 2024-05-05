package roomescape.controller.theme;

import roomescape.domain.Theme;

public record ReservationThemeResponse(Long id, String name) {

    public static ReservationThemeResponse from(final Theme theme) {
        return new ReservationThemeResponse(theme.getId(), theme.getName());
    }
}
