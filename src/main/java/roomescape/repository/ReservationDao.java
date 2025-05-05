package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.service.reservation.Reservation;

public interface ReservationDao {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    void deleteById(long id);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(Long id);

    boolean existsByDateAndTimeAndTheme(LocalDate date, long timeId, long themeId);
}
