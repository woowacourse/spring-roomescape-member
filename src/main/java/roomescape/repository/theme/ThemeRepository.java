package roomescape.repository.theme;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;
import roomescape.entity.ThemeEntity;

public interface ThemeRepository {
    ThemeEntity add(Theme theme);

    List<ThemeEntity> findAll();

    int deleteById(Long id);

    Optional<ThemeEntity> findById(Long id);
}
