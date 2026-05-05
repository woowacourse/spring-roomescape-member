package roomescape.repository;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRespository {

    Long save(Theme theme);

    List<Theme> getAll();
    void delete(Long id);
}
