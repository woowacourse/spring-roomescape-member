package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {

    Theme save(final Theme theme);

    List<Theme> findAll();

    void deleteById(final Long id);

    Optional<Theme> findById(final Long id);
}
