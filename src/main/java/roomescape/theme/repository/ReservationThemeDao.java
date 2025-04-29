package roomescape.theme.repository;

import roomescape.theme.entity.ReservationThemeEntity;

import java.util.List;
import java.util.Optional;

public interface ReservationThemeDao {
    ReservationThemeEntity save(ReservationThemeEntity entity);

    List<ReservationThemeEntity> findAll();

    boolean deleteById(Long id);

    Optional<ReservationThemeEntity> findById(Long id);

    Optional<ReservationThemeEntity> findByName(String name);
}
