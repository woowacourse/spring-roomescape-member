package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {

    Optional<Theme> save(Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    int deleteById(long id);
}
