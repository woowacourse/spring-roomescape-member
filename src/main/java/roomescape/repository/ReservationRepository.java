package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.repository.projection.ReservationEntity;

public interface ReservationRepository {
    List<ReservationEntity> findAll();

    ReservationEntity save(Reservation reservation, Long timeId, Long themeId);

    void deleteById(Long id);

    boolean existsByDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId);

    boolean existsByTimeId(Long timeId);

    List<ReservationEntity> findByNameOrderByDateAscTimeAsc(String name);

    Optional<ReservationEntity> findById(Long id);

    boolean existsByDateAndTimeAndThemeExcludingId(LocalDate date, Long timeId, Long themeId, Long excludeId);

    void updateDateAndTime(Long id, LocalDate date, Long timeId);

}
