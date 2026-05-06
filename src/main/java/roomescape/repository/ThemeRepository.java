package roomescape.repository;

import roomescape.domain.Theme;

import java.util.List;

public interface ThemeRepository {
    List<Theme> findAll();

    Theme save(Theme theme);

    void delete(Long id);
}
