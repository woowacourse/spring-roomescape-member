package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.model.Theme;

public interface ThemeRepository {

    Optional<Theme> findById(long id);

    long save(Theme theme);

    boolean removeById(long id);

    List<Theme> findAll();
}
