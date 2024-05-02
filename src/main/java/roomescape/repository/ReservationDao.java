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

    boolean existByDateAndTimeId(LocalDate date, Long timeId);

    void deleteById(Long id);

    List<ReservationTime> findByDateAndTheme(LocalDate date, Long themeId);

}
