package roomescape.domain;

import java.util.List;

public interface ThemeRepository {

    Theme save(Theme theme);

    Theme findById(Long themeId);

    List<Theme> findAll();

    int deleteById(Long id);

    List<Theme> findPopular(int count);

    boolean existByName(String name);
}
