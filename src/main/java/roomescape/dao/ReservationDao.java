package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAll();

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    Reservation add(Reservation reservation);

    int deleteById(Long id);

    boolean existByDateTimeAndTheme(LocalDate date, Long timeId, Long themeId);
}
