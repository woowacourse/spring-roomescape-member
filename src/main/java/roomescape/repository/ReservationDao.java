package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.service.reservation.Reservation;

public interface ReservationDao {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(long id);

    boolean isExistsByDateAndTimeId(LocalDate date, long timeId);

    boolean isExistsByTimeId(long timeId);

    boolean isExistsByThemeId(Long id);

    List<Reservation> findAllByDateAndThemeId(LocalDate date, long themeId);
}
