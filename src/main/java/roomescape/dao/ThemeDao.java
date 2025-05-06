package roomescape.dao;

import java.util.List;
import roomescape.entity.Theme;

public interface ThemeDao {

    List<Theme> findAll();

    Theme findById(Long id);

    List<Theme> findAllById(List<Long> themeIds);

    Long create(Theme theme);

    void deleteById(Long idRequest);
}
