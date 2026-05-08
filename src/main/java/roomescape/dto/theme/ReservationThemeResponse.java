package roomescape.dto.theme;

import roomescape.domain.reservationTheme.ReservationTheme;

public record ReservationThemeResponse(long id, String name, String description, String imageUrl) {
    public static ReservationThemeResponse from(ReservationTheme reservationTheme) {
        return new ReservationThemeResponse(
                reservationTheme.id(),
                reservationTheme.name(),
                reservationTheme.description(),
                reservationTheme.imageUrl());
    }
}
