package roomescape.reservation.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.ReservationTime;

public interface ReservationTimeRepository {

    Long saveAndReturnId(ReservationTime time);

    List<ReservationTime> findAll();

    int deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    Boolean existSameStartAt(LocalTime time);

}
