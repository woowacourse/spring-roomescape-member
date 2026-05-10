package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    List<Theme> getPopularTop10Themes(LocalDate start, LocalDate end);

    Long save(Theme theme);

    void deleteById(Long id);

    default Theme getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마 ID입니다."));
    }
}
