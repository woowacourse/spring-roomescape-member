package roomescape.theme.domain;

import java.util.List;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme save(Theme theme);

    void deleteById(ThemeId id);
}
