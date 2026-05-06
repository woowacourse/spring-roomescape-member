package roomescape.domain.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.entity.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    Theme save(Theme theme);

    void deleteById(Long id);
}
