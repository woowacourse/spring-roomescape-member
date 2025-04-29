package roomescape.dao;

import java.util.List;
import roomescape.dto.ReservationTimeResponse;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

public interface ReservationTimeDao {
    public ReservationTime save(ReservationTime time);
    public boolean deleteById(Long id);
    public List<ReservationTime> findAll();
    public ReservationTime findById(Long id);
}
