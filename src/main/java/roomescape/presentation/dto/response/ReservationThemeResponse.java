package roomescape.presentation.dto.response;

import roomescape.domain.model.Theme;

public record ReservationThemeResponse(
        String name
) {

    public static ReservationThemeResponse from(Theme theme) {
        return new ReservationThemeResponse(
                theme.getName()
        );
    }
}
