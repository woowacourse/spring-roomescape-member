package roomescape.repository.reservation;

import java.util.List;
import roomescape.domain.reservation.Theme;

public interface ThemeRepository {

    Theme findById(Long id);

    List<Theme> findAll();

    Theme save(Theme theme);

    void deleteById(Long id);

    List<Theme> findPopularThemes(int period, int maxResults);

    boolean existsByName(String name);
}
