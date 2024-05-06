package roomescape.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservationtime.ReservationTime;

@Repository
public interface ReservationTimeDao {

    List<ReservationTime> readAll();

    Optional<ReservationTime> readById(long id);

    ReservationTime create(ReservationTime reservationTime);

    boolean exist(long id);

    boolean exist(ReservationTime reservationTime);

    void delete(long id);
}
