package roomescape.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;

@Repository
public interface ReservationDao {

    List<Reservation> readAll();

    Reservation create(Reservation reservation);

    Boolean exist(long id);

    void delete(long id);
}
