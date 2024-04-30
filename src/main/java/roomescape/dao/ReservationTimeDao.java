package roomescape.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservationtime.ReservationTime;

@Repository
public interface ReservationTimeDao {

    List<ReservationTime> readAll();

    ReservationTime readById(long id);

    ReservationTime create(ReservationTime reservationTime);

    Boolean exist(long id);

    Boolean exist(ReservationTime reservationTime);

    void delete(long id);
}
