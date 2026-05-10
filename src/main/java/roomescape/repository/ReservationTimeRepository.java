package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    boolean existsByStartAt(LocalTime time);

    List<ReservationTime> findAllByPaging(int page, int size);

    List<ReservationTime> findTimeSlotsForReservationStatus();
}
