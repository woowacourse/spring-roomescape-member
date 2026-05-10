package roomescape.domain.reservation.mapper;

import roomescape.domain.reservation.dto.response.ReservationCreateResponseDTO;
import roomescape.domain.reservation.dto.response.ReservationResponseDTO;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.mapper.ThemeMapper;
import roomescape.domain.time.mapper.TimeMapper;

public final class ReservationMapper {

    private ReservationMapper() {

    }

    public static ReservationResponseDTO toResponseDTO(Reservation reservation) {
        return new ReservationResponseDTO(reservation.getId(), reservation.getName(), reservation.getDate(),
            TimeMapper.toResponseDTO(reservation.getTime()), ThemeMapper.toResponseDTO(reservation.getTheme()));
    }

    public static ReservationCreateResponseDTO toCreateResponseDTO(Reservation reservation) {
        return new ReservationCreateResponseDTO(reservation.getId(), reservation.getName(), reservation.getDate(),
            reservation.getTime().getId(), reservation.getTheme().getId());
    }
}
