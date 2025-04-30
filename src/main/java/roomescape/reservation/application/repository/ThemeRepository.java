package roomescape.reservation.application.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.presentation.dto.ThemeRequest;

public interface ThemeRepository {
    Long insert(ThemeRequest themeRequest);

    List<Theme> findAllThemes();

    void delete(Long id);

    Optional<Theme> findById(Long themeId);
}
