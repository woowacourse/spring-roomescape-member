package roomescape.reservation.repository;

import roomescape.reservation.entity.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme save(Theme theme);

    List<Theme> findAll();

    boolean deleteById(Long id);

    Optional<Theme> findById(Long id);

    Optional<Theme> findByName(String name);

    List<Theme> findPopularDescendingUpTo(LocalDate startDate, LocalDate endDate, int limit);
}
