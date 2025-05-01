package roomescape.time.repository;

import java.time.LocalTime;
import java.util.List;
import roomescape.time.domain.ReservationTime;

public interface ReservationTimeRepository {

    Long saveAndReturnId(ReservationTime time);

    List<ReservationTime> findAll();

    int deleteById(Long id);

    ReservationTime findById(Long id);

    Boolean existSameStartAt(LocalTime time);

}
