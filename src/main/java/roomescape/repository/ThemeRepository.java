package roomescape.repository;

import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Optional<Theme> findById(Long themeId);

    List<Theme> findAll();

    Theme save(Theme theme);

    void deleteById(Long themeId);

    boolean existById(Long themeId);

    List<Theme> findPopularThemes(LocalDate startAt, LocalDate endAt);
}
