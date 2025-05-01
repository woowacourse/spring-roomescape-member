package roomescape.reservation.application.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.presentation.dto.ThemeRequest;

public interface ThemeRepository {
    Theme insert(ThemeRequest themeRequest);

    List<Theme> findAllThemes();

    void delete(Long id);

    Optional<Theme> findById(Long themeId);

    List<Theme> findPopularThemes();
}
