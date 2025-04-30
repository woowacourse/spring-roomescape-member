package roomescape.domain.repository;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRepository {
    Long save(Theme theme);

    Theme findById(Long id);

    List<Theme> findAll();

    void deleteById(Long id);
}
