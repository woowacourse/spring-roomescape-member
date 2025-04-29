package roomescape.domain.reservation.repository;

import java.util.List;
import roomescape.domain.reservation.entity.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme save(Theme Theme);

    void deleteById(Long id);
}
