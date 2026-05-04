package roomescape.domain.theme;

import java.util.List;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme save(Theme theme);

    int deleteById(Long id);
}
