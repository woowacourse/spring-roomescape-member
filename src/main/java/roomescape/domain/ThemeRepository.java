package roomescape.domain;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme save(final Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(final Long id);

    void deleteById(final Long id);
}
