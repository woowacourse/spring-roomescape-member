package roomescape.dao;

import java.util.List;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    ReservationTime findById(long id);

    long create(ReservationTime time);

    void deleteById(Id id);
}
