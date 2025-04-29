package roomescape.reservation.application.repository;

import java.util.List;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.presentation.dto.ThemeRequest;

public interface ThemeRepository {
    Long insert(ThemeRequest themeRequest);

    List<Theme> findAllThemes();
}
