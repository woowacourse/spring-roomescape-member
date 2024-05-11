package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public interface ThemeRepository {

    List<Theme> findAll();

    Theme findByThemeId(Long themeId);

    List<Theme> findHotThemesByDurationAndCount(LocalDate start, LocalDate end, Integer limit, Integer offset);

    Theme save(Theme theme);

    int deleteById(Long id);
}
