package roomescape.theme.domain;

import roomescape.theme.domain.exception.ThemeNotFoundException;
import java.util.Optional;

public interface ThemeRepository {
    Theme save(Theme theme);
    void delete(Long id);
    boolean existsThemeById(Long id);
    Optional<Theme> findById(Long id);

    default Theme getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new ThemeNotFoundException("존재하지 않은 테마입니다."));
    }
}
