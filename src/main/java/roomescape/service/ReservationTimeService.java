package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao timeDao;

    public ReservationTimeService(ReservationTimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<ReservationTime> findAll() {
        return timeDao.read();
    }

    public ReservationTime create(LocalTime startAt) {
        ReservationTime time = new ReservationTime(startAt);
        return timeDao.create(time);
    }

    public void delete(Long id) {
        timeDao.delete(id);
    }
}
