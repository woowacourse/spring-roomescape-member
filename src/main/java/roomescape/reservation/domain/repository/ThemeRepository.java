package roomescape.reservation.domain.repository;

import java.util.List;
import roomescape.reservation.domain.Theme;

public interface ThemeRepository {

    Long saveAndReturnId(Theme theme);

    List<Theme> findAll();

    int deleteById(Long id);

}
