package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;
import roomescape.service.dto.PopularTheme;

public interface ThemeRepository {
    List<ThemeEntity> findAll();

    Optional<ThemeEntity> findById(Long id);
    
    ThemeEntity save(Theme theme);

    void deleteById(Long id);

    List<PopularTheme> findPopular();
}
