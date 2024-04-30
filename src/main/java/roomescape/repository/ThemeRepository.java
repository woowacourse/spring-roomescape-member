package roomescape.repository;

import roomescape.domain.Theme;

import java.util.Optional;

public interface ThemeRepository {

    Optional<Theme> findById(Long themeId);
}
