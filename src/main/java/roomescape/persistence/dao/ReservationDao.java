package roomescape.persistence.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;

public interface ReservationDao {

    Long save(Reservation reservation);

    List<Reservation> findAll();

    boolean remove(Long id);

    boolean existsByDateAndTimeAndTheme(LocalDate date, PlayTime time, Theme theme);
}
