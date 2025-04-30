package roomescape.persistence.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;

public interface ReservationDao {

    Long save(Reservation reservation);

    List<Reservation> findAll();

    boolean remove(Long id);

    boolean existsByDateAndTime(LocalDate date, PlayTime time);
}
