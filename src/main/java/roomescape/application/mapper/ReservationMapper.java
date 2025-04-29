package roomescape.application.mapper;

import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.ReservationResponse;

public class ReservationMapper {

    public static Reservation toDomain(ReservationRequest request, ReservationTime reservationTime) {
        return Reservation.withoutId(request.name(), request.date(), reservationTime);
    }

    public static ReservationResponse toDto(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
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
