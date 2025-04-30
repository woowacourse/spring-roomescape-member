package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;
import roomescape.entity.ReservationEntity;

public interface ReservationRepository {
    ReservationEntity add(Reservation reservation);

    int deleteById(Long id);

    List<ReservationEntity> findAll();

    boolean existsByDateAndTime(LocalDate date, Long id);

    boolean existsByTimeId(Long id);
}
