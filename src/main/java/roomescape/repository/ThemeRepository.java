package roomescape.repository;

import roomescape.domain.Theme;

import java.util.List;

public interface ThemeRepository {

    Theme save(Theme theme);

    List<Theme> findAll();
}
