package roomescape.dto.response;

import roomescape.domain.Theme;

public record ReservationThemeResponse(
    String name
) {

    public static ReservationThemeResponse from(Theme theme) {
        return new ReservationThemeResponse(
            theme.getName()
        );
    }
}
