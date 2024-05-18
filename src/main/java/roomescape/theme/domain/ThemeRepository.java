package roomescape.theme.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository {

    List<Theme> findAll();

    Theme findByThemeId(Long themeId);

    List<Theme> findHotThemesByDurationAndCount(LocalDate start, LocalDate end, Integer page, Integer size);

    Theme save(Theme theme);

    int deleteById(Long id);
}
