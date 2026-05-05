package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {
    List<Theme> findAll();
    Optional<Theme> findById(Long id);
    List<Theme> findByStatus(boolean status);
    Theme save(Theme theme);
    boolean updateStatus(Theme theme);
}
