package roomescape.presentation.mapper;

import roomescape.business.domain.Reservation;
import roomescape.presentation.dto.ReservationResponseDto;

public final class ReservationMapper {

    private ReservationMapper() {
    }

    public static ReservationResponseDto toResponse(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeMapper.toResponse(reservation.getTime()),
                ReservationThemeMapper.toResponse(reservation.getTheme())
        );
    }
}
