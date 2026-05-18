package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    List<Theme> findByStatus(boolean status);

    Theme save(Theme theme);

    Theme updateStatus(Theme theme);

    List<Theme> findPopularThemes(LocalDate startDate, LocalDate endDate, int limit, ReservationStatus status);

}
