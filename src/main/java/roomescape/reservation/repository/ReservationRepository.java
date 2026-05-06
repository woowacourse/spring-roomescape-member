package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;

public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(long id);

    void deleteById(long id);

    Reservation save(Reservation reservation);

    boolean existsByDateAndTimeId(LocalDate date, long timeId);

    List<Long> findAllByDateAndThemeId(LocalDate date, long themeId);

    List<Theme> findPopularThemes(int period, int limit, LocalDate now);
}
