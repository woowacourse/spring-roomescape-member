package roomescape.domain;

import java.util.List;

public interface ThemeRepository {
    Theme save(Theme theme);

    List<Theme> findAll();

    void deleteById(long id);
}
