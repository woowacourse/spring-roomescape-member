package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRespository {

    Theme save(Theme theme);

    List<Theme> getAll();

    void delete(Long id);

    Optional<Theme> findById(Long id);
}
