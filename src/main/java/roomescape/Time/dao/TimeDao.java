package roomescape.Time.dao;

import java.util.List;
import roomescape.Time.domain.Time;

public interface TimeDao {

    Time save(Time reservationTime);

    List<Time> findAllOrderByReservationTime();

    Time findById(long reservationTimeId);

    void deleteById(long reservationTimeId);
}
