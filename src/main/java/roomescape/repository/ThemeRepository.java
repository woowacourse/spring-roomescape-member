package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.model.Theme;

public interface ThemeRepository {

    Optional<Theme> findById(final long id);

    long save(final Theme theme);

    boolean removeById(final long id);

    List<Theme> findAll();
}
