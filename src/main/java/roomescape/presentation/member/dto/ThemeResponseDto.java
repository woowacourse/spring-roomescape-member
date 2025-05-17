package roomescape.presentation.member.dto;

import roomescape.business.domain.reservation.ReservationTheme;

public record ThemeResponseDto(
        long id,
        String name,
        String description,
        String thumbnail
) {

    public static ThemeResponseDto toResponse(ReservationTheme reservationTheme) {
        return new ThemeResponseDto(
                reservationTheme.getId(),
                reservationTheme.getName(),
                reservationTheme.getDescription(),
                reservationTheme.getThumbnail()
        );
    }
}
