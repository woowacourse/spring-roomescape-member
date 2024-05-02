package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.theme.model.Theme;

public interface ThemeRepository {

    Theme save(Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    List<Theme> findOrderByReservation();

    void deleteById(Long id);
}
