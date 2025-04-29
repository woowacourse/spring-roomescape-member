package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationDAO {

    long insert(Reservation reservation);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId);

    List<Reservation> findAll();

    Optional<Reservation> findById(long id);

    boolean deleteById(long id);
}
