package roomescape.repository;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme findById(long id);

    Theme save(Theme theme);

    void deleteById(long id);
}
