package roomescape.time.repository;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.time.dao.TimeDao;
import roomescape.time.domain.Time;

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

    public int findByStartAt(LocalTime startAt) {
        return timeDao.countByStartAt(startAt);
    }
}
