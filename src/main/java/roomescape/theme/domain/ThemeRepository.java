package roomescape.theme.domain;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(ThemeId id);

    Theme save(Theme theme);

    void deleteById(ThemeId id);
}
