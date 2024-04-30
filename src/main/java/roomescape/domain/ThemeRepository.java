package roomescape.domain;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    List<Theme> findAll();

    Theme create(Theme theme);

    void removeById(Long id);

    Optional<Theme> findById(Long id);
}
