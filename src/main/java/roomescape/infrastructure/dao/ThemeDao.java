package roomescape.infrastructure.dao;

import roomescape.domain.model.Theme;

import java.util.List;

public interface ThemeDao {

    List<Theme> findAll();

    Theme save(Theme theme);

    int deleteById(Long id);

    Theme findById(Long id);

    boolean existByName(String name);

    List<Theme> findPopular(int count);
}
