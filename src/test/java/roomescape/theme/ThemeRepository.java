package roomescape.theme;

import roomescape.theme.domain.Theme;

public interface ThemeRepository {
    Theme save(Theme theme);
    Boolean existsByNameAndDescription(Theme theme);
    Integer delete(long id);
}
