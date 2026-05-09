package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    Theme save(Theme theme);
    Optional<Theme> findById(Long id);
    List<Theme> findAll();
    List<Theme> findTopNByPeriod(LocalDate startAt, LocalDate endAt, ThemeSortType sortType, Long limit);
    void deleteById(Long id);
}
