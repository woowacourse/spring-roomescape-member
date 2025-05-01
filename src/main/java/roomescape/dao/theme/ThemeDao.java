package roomescape.dao.theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeDao {

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    Theme create(Theme theme);

    boolean deleteIfNoReservation(long id);

    List<Theme> findPopularThemesInRecentSevenDays(LocalDate startDate, LocalDate endDate);
}
