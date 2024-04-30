package roomescape.repository;

import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Optional<Theme> findById(Long themeId);

    List<Theme> findAll();
}
