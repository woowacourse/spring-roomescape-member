package roomescape.dao;

import java.util.List;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.ReservationTime;

public interface TimeDao {

    List<ReservationTime> findAll();

    ReservationTime findById(long id);

    long create(ReservationTime time);

    void deleteById(Id id);
}
