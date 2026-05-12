package roomescape.domain.reservation.repository;

import java.util.List;
import roomescape.domain.reservation.entity.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(Long id);
}
