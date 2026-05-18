package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.common.exception.ResourceNotFoundException;
import roomescape.entity.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    List<Theme> getPopularTop10Themes(LocalDate start, LocalDate end);

    Long save(Theme theme);

    void deleteById(Long id);

    default Theme getById(Long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 테마입니다."));
    }
}
