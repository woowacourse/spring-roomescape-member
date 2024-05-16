package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {

  Optional<Theme> findById(Long themeId);

  List<Theme> findAll();

  Theme save(Theme theme);

  void deleteById(Long themeId);

  boolean existById(Long themeId);

  List<Theme> findPopularThemes(ReservationDate startAt, ReservationDate endAt,
      int maximumThemeCount);
}
