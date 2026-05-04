package roomescape.reservation.service;

import java.util.List;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;

public interface ReservationService {
    List<Reservation> getReservations();
    Reservation save(ReservationSaveServiceDto reservation);
    void deleteById(long id);
}
