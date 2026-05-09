package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;
import roomescape.repository.projection.ReservationTimeEntity;

public interface ReservationTimeRepository {
    List<ReservationTimeEntity> findAll();

    Optional<ReservationTimeEntity> findById(Long id);

    ReservationTimeEntity save(ReservationTime time);

    void deleteById(Long id);

    List<ReservationTimeEntity> findAvailable(LocalDate date, Long themeId);
}
