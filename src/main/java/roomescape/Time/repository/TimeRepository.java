package roomescape.Time.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.Time.dao.TimeDao;
import roomescape.Time.domain.Time;

@Repository
public class TimeRepository {
    private final TimeDao timeDao;

    public TimeRepository(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public Time saveReservationTime(Time reservationTime) {
        return timeDao.save(reservationTime);
    }

    public List<Time> findAllReservationTimes() {
        return timeDao.findAllOrderByReservationTime();
    }

    public void deleteReservationTimeById(long reservationTimeId) {
        timeDao.deleteById(reservationTimeId);
    }
}
