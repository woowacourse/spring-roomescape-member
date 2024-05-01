package roomescape.domain;

import java.util.List;

public interface ThemeRepository {
    Theme create(Theme theme);

    List<Theme> findAll();

    void deleteById(long id);
}
