package roomescape.application.mapper;

import java.util.List;
import roomescape.application.dto.ReservationDto;
import roomescape.application.dto.ThemeDto;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.presentation.dto.request.ReservationRequest;

public class ReservationMapper {

    public static Reservation toDomain(ReservationRequest request, Theme theme, ReservationTime reservationTime) {
        return Reservation.withoutId(request.name(), theme, request.date(), reservationTime);
    }

    public static ReservationDto toDto(Reservation reservation) {
        ThemeDto dto = ThemeMapper.toDto(reservation.getTheme());
        return new ReservationDto(
                reservation.getId(),
                reservation.getName(),
                ThemeMapper.toDto(reservation.getTheme()),
                reservation.getReservationDate(),
                ReservationTimeMapper.toDto(reservation.getReservationTime())
        );
    }

    public static List<ReservationDto> toDtos(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationMapper::toDto)
                .toList();
    }
}
