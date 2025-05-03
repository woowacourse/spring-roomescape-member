package roomescape.dao;

import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    ReservationTime findById(Long id);

    Long create(ReservationTime time);

    void deleteById(Long id);
}
