package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.entity.ThemeEntity;

public interface ThemeRepository {
    Long save(ThemeEntity themeEntity);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    void deleteById(Long id);
}
