package roomescape.dao;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDao {

    List<Theme> findAll();

    Theme findById(Long id);

    Long create(Theme theme);

    void deleteById(Long idRequest);
}
