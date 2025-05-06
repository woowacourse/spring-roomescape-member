package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    void deleteById(long id);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(Long id);

    boolean existsByDateAndTimeAndTheme(LocalDate date, long timeId, long themeId);
}
