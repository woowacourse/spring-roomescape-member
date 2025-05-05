package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.model.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Long save(final Theme theme);

    Optional<Theme> findById(final Long id);

    Boolean removeById(final Long id);
}
