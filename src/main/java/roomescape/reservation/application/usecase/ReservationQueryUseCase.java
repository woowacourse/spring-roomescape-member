package roomescape.reservation.application.usecase;

import roomescape.reservation.domain.Reservation;

import java.util.List;

public interface ReservationQueryUseCase {

    List<Reservation> getAll();
}
