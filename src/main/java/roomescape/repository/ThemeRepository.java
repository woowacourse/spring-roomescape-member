package roomescape.repository;

import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {

    boolean existByNameAndIsDeletedFalse(String name);

    Theme save(Theme theme);

    void update(Theme theme);

    Optional<Theme> findById(long id);
}
