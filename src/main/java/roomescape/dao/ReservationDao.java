package roomescape.dao;

import java.util.List;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.Reservation;

public interface ReservationDao {

    List<Reservation> findAll();

    long create(Reservation reservation);

    void deleteById(Id id);

    Boolean existByTimeId(Id timeId);

    Boolean existBySameDateTime(Reservation reservation);
}
