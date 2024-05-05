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

    public Time save(Time time) {
        return timeDao.save(time);
    }

    public List<Time> findAllReservationTimesInOrder() {
        return timeDao.findAllReservationTimesInOrder();
    }

    public List<Time> findAllReservationTimes() {
        return timeDao.findAllReservationTimesInOrder();
    }

    public void deleteById(long reservationTimeId) {
        timeDao.deleteById(reservationTimeId);
    }

    public int countByStartAt(LocalTime startAt) {
        return timeDao.countByStartAt(startAt);
    }
}
