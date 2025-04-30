package roomescape.application.mapper;

import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.ReservationResponse;

public class ReservationMapper {

    public static Reservation toDomain(ReservationRequest request, Theme theme, ReservationTime reservationTime) {
        return Reservation.withoutId(request.name(), theme, request.date(), reservationTime);
    }

    public static ReservationResponse toDto(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                ThemeMapper.toDto(reservation.getTheme()),
                reservation.getReservationDate(),
                ReservationTimeMapper.toDto(reservation.getReservationTime())
        );
    }

    public static List<ReservationResponse> toDtos(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationMapper::toDto)
                .toList();
    }
}
