package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {
    Long save(Theme theme);

    Optional<Theme> findById(Long id);

    long countByName(String name);

    List<Theme> findAll();

    void deleteById(Long id);
}
