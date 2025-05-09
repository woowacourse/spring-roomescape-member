package roomescape.presentation.admin.dto;

import roomescape.business.domain.reservation.ReservationTheme;

public record ReservationThemeResponseDto(
        long id,
        String name,
        String description,
        String thumbnail
) {

    public static ReservationThemeResponseDto toResponse(ReservationTheme reservationTheme) {
        return new ReservationThemeResponseDto(
                reservationTheme.getId(),
                reservationTheme.getName(),
                reservationTheme.getDescription(),
                reservationTheme.getThumbnail()
        );
    }
}
