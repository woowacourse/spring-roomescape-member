package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {
    Theme save(Theme theme);

    List<Theme> read();

    void delete(Long id);

    Optional<Theme> findById(Long id);
}
