package roomescape.repository;

import roomescape.domain.Theme;

public interface ThemeRepository {
    boolean existByName(String name);
    Theme save(Theme theme);
}
