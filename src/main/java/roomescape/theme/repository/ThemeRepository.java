package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {

    Theme save(Theme theme);

    void deleteById(Long id);

    Optional<Theme> findById(Long id);

    boolean existByName(String name);

    List<Theme> findAll();
}
