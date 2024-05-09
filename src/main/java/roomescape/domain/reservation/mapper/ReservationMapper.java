package roomescape.domain.reservation.mapper;

import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.domain.ReservationTime;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationSaveRequest;
import roomescape.domain.theme.domain.Theme;

public class ReservationMapper {

    public ReservationResponse mapToResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    public ReservationResponse mapToResponse(Long id, Reservation reservation) {
        return new ReservationResponse(id, reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    public Reservation mapToReservation(ReservationSaveRequest request, ReservationTime reservationTime, Theme theme) {
        return new Reservation(request.id(), request.name(), request.date(), reservationTime, theme);
    }
}
