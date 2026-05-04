package roomescape.dto.theme;

import roomescape.domain.ReservationTheme.ReservationTheme;

public record ThemeResponse(long id, String name, String description, String imageUrl) {
    public static ThemeResponse from(ReservationTheme reservationTheme) {
        return new ThemeResponse(reservationTheme.id(), reservationTheme.name(), reservationTheme.description(), reservationTheme.imageUrl());
    }
}
