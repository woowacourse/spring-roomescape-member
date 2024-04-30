package roomescape.repository;

import java.util.List;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTimeRequest reservationTimeRequest);

    List<ReservationTime> findAll();

    void delete(long id);
}
