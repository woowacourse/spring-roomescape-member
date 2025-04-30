package roomescape.repository;

import roomescape.entity.Theme;

import java.util.List;

public interface ThemeRepository {
    Theme save(Theme theme);

    List<Theme> findAll();

    boolean existById(long id);

    void deleteById(long id);

    Theme findById(long id);
}
