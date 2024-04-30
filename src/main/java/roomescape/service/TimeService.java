package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeCreateRequest;

import java.util.List;

@Service
public class TimeService {
    private final TimeDao timeDao;
    private final ReservationDao reservationDao;

    public TimeService(TimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTime> readTimes() {
        return timeDao.readTimes();
    }

    public ReservationTime createTime(TimeCreateRequest dto) {
        return timeDao.createTime(dto.createReservationTime());
    }

    public boolean deleteTime(long id) {
        if (reservationDao.readReservationCountByTimeId(id) == 0) {
            timeDao.deleteTime(id);
            return true;
        }
        return false;
    }
}
