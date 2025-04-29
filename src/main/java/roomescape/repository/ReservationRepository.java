package roomescape.repository;

import java.util.List;
import roomescape.entity.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation, long timeId);

    void deleteById(long id);
}
