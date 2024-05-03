package roomescape.repository;

import java.util.List;
import roomescape.domain.theme.Theme;

public interface ThemeRepository {

    Long save(Theme theme);

    List<Theme> findAll();

    Theme findById(Long id);

    void delete(Long id);

    Boolean existsById(Long id);

    Boolean existsByName(String name);
}
