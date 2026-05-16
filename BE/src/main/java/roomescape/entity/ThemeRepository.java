package roomescape.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.NotFoundException;

public interface ThemeRepository {
    Theme save(Theme theme);
    Optional<Theme> findById(Long id);
    List<Theme> findAll();
    List<Theme> findTopNByPeriod(LocalDate startAt, LocalDate endAt, ThemeSortType sortType, Long limit);
    void deleteById(Long id);
    Theme getById(Long id);
}
