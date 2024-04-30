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

    public void deleteTime(long id) {
        if (reservationDao.isReservationsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간을 사용하는 예약이 존재합니다.");
        }
        timeDao.deleteTime(id);
    }
}
