package roomescape.domain.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {
    Long save(Theme theme);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    void deleteById(Long id);
}
