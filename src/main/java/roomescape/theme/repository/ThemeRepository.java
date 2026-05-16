package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.dto.ThemeBestServiceDto;

public interface ThemeRepository {
    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    Theme save(Theme theme);

    boolean existsById(Long id);

    boolean deleteById(Long id);

    List<Theme> findBestThemesByDate(ThemeBestServiceDto themeBestServiceDto);
}
