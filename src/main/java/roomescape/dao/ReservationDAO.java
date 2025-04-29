package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDAO {

    List<Reservation> findAll();

    boolean existsByDateAndTimeId(LocalDate date, long time_id);

    long insert(Reservation reservation);

    boolean deleteById(long id);
}
