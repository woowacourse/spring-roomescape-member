package roomescape.repository;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    List<ReservationEntity> findAll();

    ReservationEntity save(Reservation reservation, Long timeId, Long themeId);

    void deleteById(Long id);
}
