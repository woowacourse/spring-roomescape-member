package roomescape.mapper;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;

public class ReservationMapper {

    public ReservationResponse mapToResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    public ReservationResponse mapToResponse(Long id, Reservation reservation) {
        return new ReservationResponse(id, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    public Reservation mapToReservation(ReservationSaveRequest request, ReservationTime reservationTime) {
        return new Reservation(request.id(), request.name(), request.date(), reservationTime);
    }
}
