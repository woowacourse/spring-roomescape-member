package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Theme;
import roomescape.dto.theme.PopularThemeResponse;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme findById(Long id);

    Long save(Theme theme);

    void deleteById(Long id);

    List<PopularThemeResponse> findPopularThemes(LocalDate startDate, LocalDate endDate, Integer limit);
}
