package roomescape.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public interface ThemeRepository {

    List<Theme> findAll();

    Theme findByThemeId(Long themeId);

    List<Theme> findWeeklyHotThemes();

    Theme save(Theme theme);

    int deleteById(Long id);
}
