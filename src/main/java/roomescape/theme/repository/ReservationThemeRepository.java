package roomescape.theme.repository;

import roomescape.theme.entity.ReservationThemeEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationThemeRepository {
    ReservationThemeEntity save(ReservationThemeEntity entity);

    List<ReservationThemeEntity> findAll();

    boolean deleteById(Long id);

    Optional<ReservationThemeEntity> findById(Long id);

    Optional<ReservationThemeEntity> findByName(String name);

    List<ReservationThemeEntity> findPopularThemesByDateRangeAndLimit(LocalDate startDate, LocalDate endDate, int limit);
}
