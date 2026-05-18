package roomescape.repository.theme;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeWithCount;
import roomescape.dto.theme.PopularConditionRequest;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    Theme addTheme(Theme theme);
    List<Theme> getAllTheme();
    Optional<Theme> getTheme(long id);
    void deleteTheme(long id);
    List<ThemeWithCount> getPopularTheme(PopularConditionRequest popularConditionRequest);
    boolean existsByName(String name);
}
