package roomescape.user.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.user.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao timeDao;

    public ReservationTimeService(ReservationTimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<ReservationTime> findAll() {
        return timeDao.selectAll();
    }

    public ReservationTime add(LocalTime startAt) {
        ReservationTime time = new ReservationTime(startAt);
        return timeDao.insert(time);
    }

    public void delete(Long id) {
        timeDao.delete(id);
    }
}
