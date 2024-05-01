package roomescape.controller.theme;

import roomescape.domain.Theme;

public record ReservationThemeResponse(String name) {

    public static ReservationThemeResponse from(final Theme theme) {
        return new ReservationThemeResponse(theme.getName());
    }
}
