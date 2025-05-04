package roomescape.infrastructure;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme save(String name, String description, String thumbnail);

    void deleteById(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findPopularThemeDuringAWeek(int limit, LocalDate now);
}
