package roomescape.repository;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRepository {

    Theme save(Theme theme);

    int deleteById(Long id);

    List<Theme> findAll();

    Theme findById(Long id);

    boolean isNameExists(String name);

    List<Theme> findPopular(int count);
}
