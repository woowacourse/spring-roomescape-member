package roomescape.repository;

import roomescape.domain.Theme;

import java.util.Optional;

public interface ThemeRepository {

    Theme save(Theme theme);

    Optional<Theme> findById(Long id);
}
