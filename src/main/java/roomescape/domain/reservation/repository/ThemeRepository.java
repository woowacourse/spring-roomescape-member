package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import roomescape.domain.reservation.entity.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    List<Theme> findThemeRankingByReservation(LocalDate startDate, LocalDate endDate, int rowCount);

    Theme save(Theme theme);

    void deleteById(Long id);

    Optional<Theme> findById(Long id);
}
