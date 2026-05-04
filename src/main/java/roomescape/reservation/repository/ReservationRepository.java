package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    List<Reservation> findAll();

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    List<Theme> findPopularThemes(int period, int limit);
}
