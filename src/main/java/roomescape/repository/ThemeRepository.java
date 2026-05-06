package roomescape.repository;

import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    List<Theme> findAll();

    Theme save(Theme theme);

    void delete(Long id);

    Optional<Theme> findById(Long id);
}
