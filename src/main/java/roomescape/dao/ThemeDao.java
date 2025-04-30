package roomescape.dao;

import java.util.List;
import roomescape.model.Theme;

public interface ThemeDao {
    Theme save(Theme theme);
    boolean deleteById(Long id);
    List<Theme> findAll();
}
