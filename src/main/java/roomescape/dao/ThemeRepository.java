package roomescape.dao;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {
    Theme save(Theme theme);

    List<Theme> findAll();

    void deleteById(long id);

    boolean existsByName(String name);

    Optional<Theme> findById(long id);
}
