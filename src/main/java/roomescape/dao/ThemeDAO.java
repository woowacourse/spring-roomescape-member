package roomescape.dao;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDAO {

    long insert(Theme theme);

    List<Theme> findAll();

    boolean deleteById(long id);
}
