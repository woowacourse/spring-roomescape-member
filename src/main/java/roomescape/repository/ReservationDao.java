package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {
    List<Reservation> findAll();

    Reservation findById(long id);

    boolean existByDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId);

    long save(Reservation reservation);

    void deleteById(Long id);

    List<Long> findTimeIdsByDateAndThemeId(LocalDate date, Long themeId);

    boolean existByTimeId(Long id);

    boolean existByThemeId(Long id);
}
