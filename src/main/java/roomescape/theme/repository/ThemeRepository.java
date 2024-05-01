package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.theme.model.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme save(Theme theme);

    Optional<Theme> findById(Long id);

    void deleteById(Long id);

    List<Theme> findOrderByReservation();
}
