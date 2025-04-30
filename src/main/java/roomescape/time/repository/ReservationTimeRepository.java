package roomescape.time.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.entity.ReservationTimeEntity;

public interface ReservationTimeRepository {

    Long save(ReservationTimeEntity reservationTimeEntity);

    Optional<ReservationTime> findById(Long id);

    List<ReservationTime> findAllByStartAt(LocalTime startAt);

    List<ReservationTime> findAll();

    void deleteById(Long id);
}
