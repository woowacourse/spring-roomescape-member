package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {
    List<Theme> findAll();

    Optional<Theme> findById(long id);

    Theme save(Theme theme);

    void delete(long id);
}
