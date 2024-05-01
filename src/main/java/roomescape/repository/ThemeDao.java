package roomescape.repository;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDao {

    List<Theme> findAll();

    Theme insert(Theme theme);

    void deleteById(Long id);
}
