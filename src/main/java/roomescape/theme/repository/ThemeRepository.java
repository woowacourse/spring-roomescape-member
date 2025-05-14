package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {
    Theme add(Theme theme);

    List<Theme> findAll();

    int deleteById(Long id);

    Optional<Theme> findById(Long id);
}
