package roomescape.reservationtime;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class ReservationTimeRepository {
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeRepository(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll();
    }

    public ReservationTime save(LocalTime startAt) {
        return reservationTimeDao.save(startAt);
    }

    public void delete(long id) {
        reservationTimeDao.delete(id);
    }

    public Optional<ReservationTime> findById(long id) {
        return reservationTimeDao.findById(id);
    }
}
