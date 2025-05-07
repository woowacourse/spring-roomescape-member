package roomescape.dao;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDao {

    List<Theme> findAll();

    Theme findById(long id);

    Long save(Theme theme);

    void deleteById(long id);
}
