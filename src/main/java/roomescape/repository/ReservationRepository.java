package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;
import roomescape.repository.projection.ReservationEntity;

public interface ReservationRepository {
    List<ReservationEntity> findAll();

    ReservationEntity save(Reservation reservation, Long timeId, Long themeId);

    void deleteById(Long id);

    boolean existsByDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId);

    boolean existsByTimeId(Long timeId);
}
