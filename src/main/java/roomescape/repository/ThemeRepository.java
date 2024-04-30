package roomescape.repository;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRepository {

    Long save(Theme theme);

    List<Theme> findAll();

    Theme findById(Long id);

    void delete(Long id);

    Boolean existId(Long id);

    Boolean existName(String name);
}
