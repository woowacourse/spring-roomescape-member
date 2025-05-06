package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAll();

    Long save(Reservation reservation);

    void deleteById(long id);

    Boolean existsByTimeId(long timeId);

    Boolean existsByDateAndTimeId(LocalDate date, long timeId);

    Boolean existsByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId);

    List<Long> findTop10ByBetweenDates(LocalDate start, LocalDate end);
}
