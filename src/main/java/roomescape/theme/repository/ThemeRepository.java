package roomescape.theme.repository;

import java.util.List;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {
    Theme save(Theme theme);
    Boolean existsByNameAndDescription(Theme theme);
    Integer delete(long id);
    List<Theme> findAll();
}
