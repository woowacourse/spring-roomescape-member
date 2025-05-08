package roomescape.presentation.mapper;

import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.presentation.dto.ReservationThemeResponseDto;

public final class ReservationThemeMapper {

    private ReservationThemeMapper() {
    }

    public static ReservationThemeResponseDto toResponse(ReservationTheme reservationTheme) {
        return new ReservationThemeResponseDto(
                reservationTheme.getId(),
                reservationTheme.getName(),
                reservationTheme.getDescription(),
                reservationTheme.getThumbnail()
        );
    }
}
