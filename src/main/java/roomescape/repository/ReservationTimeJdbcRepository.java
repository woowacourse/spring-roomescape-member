package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTimeJdbcRepository implements ReservationTimeRepository {
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeJdbcRepository(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        Long id = reservationTimeDao.insert(reservationTime);
        return new ReservationTime(id, reservationTime);
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimeDao.selectAll();
    }

    @Override
    public void deleteById(Long id) {
        reservationTimeDao.deleteById(id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return Optional.ofNullable(reservationTimeDao.selectById(id));
    }
}
