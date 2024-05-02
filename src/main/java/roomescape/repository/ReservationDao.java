package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public interface ReservationDao {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Reservation insert(Reservation reservation);

    boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    void deleteById(Long id);

    List<ReservationTime> findByDateAndTheme(LocalDate date, Long themeId);

    List<Theme> findThemeOrderByReservationCount();

}
