package roomescape.dao;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(long id);

    boolean existsReservation(String date, long id);
}
