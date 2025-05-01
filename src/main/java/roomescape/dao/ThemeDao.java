package roomescape.dao;

import java.util.List;
import roomescape.domain_entity.Theme;

public interface ThemeDao {

    Long create(Theme theme);

    List<Theme> findAll();

    void deleteById(Long idRequest);
}
