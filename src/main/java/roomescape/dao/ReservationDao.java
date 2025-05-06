package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAll();

    Long save(Reservation reservation);

    void deleteById(long id);

    boolean existsByTimeId(long timeId);

    boolean existsByDateAndTimeId(LocalDate date, long timeId);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId);

    List<Long> findTop10ByBetweenDates(LocalDate start, LocalDate end);
}
